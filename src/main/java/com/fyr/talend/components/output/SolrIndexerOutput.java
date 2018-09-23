package com.fyr.talend.components.output;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.fyr.talend.components.service.FSolrToolsService;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.UpdateHandler;
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
        Path solrConfigPath, solrIndexPath;

        solrConfigPath = new File(configuration.getSolrHomePath(), configuration.getSolrCoreName()).toPath();
        solrIndexPath = new File(solrConfigPath.toFile(), solrDataSubFolder).toPath();

        if (!configuration.getAppendIndex()) {
            service.deleteDirectory(solrIndexPath);
        }

        coreContainer = new CoreContainer(configuration.getSolrHomePath());
        coreContainer.load();
        log.info("CoreContainer successfully initialized.");

        core = coreContainer.getCore(configuration.getSolrCoreName());
        log.info("SolrCore successfully initialized.");

        ModifiableSolrParams params = new ModifiableSolrParams();
        solrQueryRequest = new LocalSolrQueryRequest(core, params);
    }

    @BeforeGroup
    public void beforeGroup() {
        // no action before a chunk is processed.
    }

    @ElementListener
    public void onNext(@Input final JsonObject defaultInput) {

        UpdateHandler updateHandler = core.getUpdateHandler();
        AddUpdateCommand cmd = new AddUpdateCommand(solrQueryRequest);

        try {
            SolrInputDocument doc = new SolrInputDocument();

            // Iterating over the entries and add them into the document
            for (String key : defaultInput.keySet()) {
                JsonValue value = defaultInput.get(key);
                if (value instanceof JsonString) {
                    doc.addField(key, ((JsonString) value).getString());
                } else if (value instanceof JsonNumber) {
                    doc.addField(key, ((JsonNumber) value).longValue());
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
        // Commit after each chunk
        try {
            core.getUpdateHandler().commit(new CommitUpdateCommand(solrQueryRequest, false));
        } catch (IOException e) {
            log.error("Committing changes was not successful.");
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void release() {
        try {
            core.getUpdateHandler().commit(new CommitUpdateCommand(solrQueryRequest, false));
            coreContainer.shutdown();
        } catch (IOException e) {
            log.error("Committing changes & closiong the SolrCore was not successful.");
            e.printStackTrace();
        }
    }
}