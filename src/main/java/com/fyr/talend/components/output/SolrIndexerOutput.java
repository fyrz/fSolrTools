package com.fyr.talend.components.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.lang.model.util.ElementScanner6;
import javax.xml.parsers.ParserConfigurationException;

import com.fyr.talend.components.service.FSolrToolsService;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.index.NoDeletionPolicy;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.CoreDescriptor;
import org.apache.solr.core.IndexDeletionPolicyWrapper;
import org.apache.solr.core.NodeConfig;
import org.apache.solr.core.SimpleFSDirectoryFactory;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.SolrIndexConfig;
import org.apache.solr.update.SolrIndexWriter;
import org.apache.solr.update.UpdateHandler;
import org.apache.solr.util.RTimerTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.AfterGroup;
import org.talend.sdk.component.api.processor.BeforeGroup;
import org.talend.sdk.component.api.processor.ElementListener;
import org.talend.sdk.component.api.processor.Input;
import org.talend.sdk.component.api.processor.Processor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Version(1) // default version is 1, if some configuration changes happen between 2 versions
            // you can add a migrationHandler
@Icon(Icon.IconType.STAR) // you can use a custom one using @Icon(value=CUSTOM, custom="filename") and
                          // adding icons/filename_icon32.png in resources
@Processor(name = "solrIndexer")
@Documentation("TODO fill the documentation for this processor")
public class SolrIndexerOutput implements Serializable {
    private static final long serialVersionUID = -1835083220459563930L;

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private final SolrIndexerOutputConfiguration configuration;
    private final FSolrToolsService service;

    private final String fileEncoding = "UTF-8";
    private final String schemaXmlFilename = "schema.xml";
    private final String solrconfigXmlFilename = "solrconfig.xml";
    private final String solrConfigSubFolder = "conf";
    private final String solrDataSubFolder = "data";

    private SolrCore core;
    private CoreContainer coreContainer;
    private SolrQueryRequest solrQueryRequest;

    public SolrIndexerOutput(@Option("configuration") final SolrIndexerOutputConfiguration configuration,
            final FSolrToolsService service) {
        this.configuration = configuration;
        this.service = service;
    }

    @PostConstruct
    public void init() {
        SolrConfig solrConfig;
        InputStreamReader schemaInputReader, solrConfigInputReader;
        InputSource schemaInputSource, solrConfigInputSource;
        Path solrConfigPath, solrSchemaPath, solrIndexPath;
        try {
            solrConfigPath = new File(configuration.getSolrHomePath(), configuration.getSolrCoreName()).toPath();
            solrSchemaPath = new File(solrConfigPath.toFile(), solrConfigSubFolder).toPath();
            solrIndexPath = new File(solrConfigPath.toFile(), solrDataSubFolder).toPath();

            if (!configuration.getAppendIndex()) {
                service.deleteDirectory(solrIndexPath);
            }

            solrConfigInputReader = new InputStreamReader(new FileInputStream(new File (solrConfigPath.toFile(), solrconfigXmlFilename)), fileEncoding);
            solrConfigInputSource = new InputSource(solrConfigInputReader);
            solrConfigInputSource.setEncoding(fileEncoding);
            solrConfig = new SolrConfig(solrConfigPath, solrconfigXmlFilename, solrConfigInputSource);

            schemaInputReader = new InputStreamReader(new FileInputStream(new File (solrSchemaPath.toFile(), schemaXmlFilename)), fileEncoding);
            schemaInputSource = new InputSource(schemaInputReader);
            schemaInputSource.setEncoding(fileEncoding);
            IndexSchema schema = new IndexSchema(solrConfig, schemaXmlFilename, schemaInputSource);
            
            coreContainer = new CoreContainer(configuration.getSolrHomePath());
            coreContainer.load();
            log.info("CoreContainer successfully initialized.");

            core = coreContainer.getCore(configuration.getSolrCoreName());
            log.info("SolrCore successfully initialized.");

            ModifiableSolrParams params = new ModifiableSolrParams();
            solrQueryRequest = new LocalSolrQueryRequest(core, params);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.error("Initializing SolrCore was not successful. See stacktrace for details.");
            e.printStackTrace();
        }

    }

    @BeforeGroup
    public void beforeGroup() {
        // if the environment supports chunking this method is called at the beginning
        // if a chunk
        // it can be used to start a local transaction specific to the backend you use
        // Note: if you don't need it you can delete it
    }

    @ElementListener
    public void onNext(@Input final JsonObject defaultInput) {
        
        UpdateHandler updateHandler = core.getUpdateHandler();
        AddUpdateCommand cmd = new AddUpdateCommand(solrQueryRequest);

        try {
            SolrInputDocument doc = new SolrInputDocument();


            for (String key : defaultInput.keySet()) {
                JsonValue value = defaultInput.get(key);
                if (value instanceof JsonString){
                    doc.addField(key, ((JsonString)value).getString());
                }
                else if (value instanceof JsonNumber) {
                    doc.addField(key, ((JsonNumber)value).longValue());
                } else {
                    // Todo
                }
            }

            cmd.solrDoc = doc;
            updateHandler.addDoc(cmd);
            
        } catch (IOException e) {
            log.error("Adding the document to the index was not successful.");
            e.printStackTrace();
        }
    }

    @AfterGroup
    public void afterGroup() {
        // symmetric method of the beforeGroup() executed after the chunk processing
        // Note: if you don't need it you can delete it
    }

    @PreDestroy
    public void release() {
        try {
            core.getUpdateHandler().commit(new CommitUpdateCommand(solrQueryRequest, false));
            coreContainer.shutdown();
        } catch (IOException e) {
            log.error("Committing & closiong the SolrCore was not successful.");
            e.printStackTrace();
        }
    }
}