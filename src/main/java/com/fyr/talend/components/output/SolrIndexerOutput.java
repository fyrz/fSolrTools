package com.fyr.talend.components.output;

import com.fyr.talend.components.helper.JsonProcessingHelper;
import com.fyr.talend.components.service.SolrRawIndexToolsService;
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
import org.talend.sdk.component.api.component.Icon.IconType;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

/**
 * This component writes a SolrIndex based on a SolrConfiguration and Generic Row input.
 * 
 */
@Version(1) // default version is 1, if some configuration changes happen between 2 versions
            // you can add a migrationHandler
// @Icon(Icon.IconType.STAR)
@Icon(value = IconType.CUSTOM, custom = "nyhc")
@Processor(name = "Indexer")
@Documentation("This component writes a SolrIndex based on a SolrConfiguration and Generic Row input.")
public class SolrIndexerOutput implements Serializable {
    // auto generated serial version uid
    private static final long serialVersionUID = -1835083220459563930L;

    // SLF4J logger
    protected final Logger log = LoggerFactory.getLogger(getClass());

    // Configuration
    private final SolrIndexerOutputConfiguration configuration;

    // Service
    private final SolrRawIndexToolsService service;

    // Path to solrHome directory
    private Path solrHome;

    // Solr CoreContainer
    private CoreContainer coreContainer;

    // SolrCore
    private SolrCore core;

    // SolrQueryRequest
    private SolrQueryRequest solrQueryRequest;

    /**
     * SolrIndexerOutput CTor
     * 
     * @param configuration SolrIndexerOutputConfiguration
     * @param service       SolrRawIndexToolsService
     */
    public SolrIndexerOutput(
            @Option("configuration") final SolrIndexerOutputConfiguration configuration,
            final SolrRawIndexToolsService service) {
        this.configuration = configuration;
        this.service = service;
    }

    /**
     * Initializer method to instantiate objects, variables throughout the component lifetime.
     * 
     */
    @PostConstruct
    public void init() {
        solrHome = new File(configuration.getSolrHomePath()).toPath();

        coreContainer = service.initCore(solrHome, configuration.getSolrCoreName(),
                configuration.getAppendIndex());
        core = coreContainer.getCore(configuration.getSolrCoreName());
        log.info("SolrCore successfully retrieved.");

        ModifiableSolrParams params = new ModifiableSolrParams();
        solrQueryRequest = new LocalSolrQueryRequest(core, params);
    }

    /**
     * Handler which is called before each chunk of rows (if applicable).
     * 
     */
    @BeforeGroup
    public void beforeGroup() {
        // no action before a chunk is processed.
    }

    /**
     * Row Handler which is called on each newly emitted row. The rows are added to the Solr index.
     * 
     * @param defaultInput javax.json.JsonObject
     */
    @ElementListener
    public void onNext(@Input final JsonObject defaultInput) {
        UpdateHandler updateHandler = core.getUpdateHandler();
        AddUpdateCommand cmd = new AddUpdateCommand(solrQueryRequest);

        try {
            SolrInputDocument doc = new SolrInputDocument();
            // Iterating over the entries and add them into the document
            for (String key : defaultInput.keySet()) {
                Object obj =
                        JsonProcessingHelper.getJavaTypeFromJsonJsonValue(defaultInput.get(key));
                if (obj != null) {
                    doc.addField(key, obj);
                }
            }

            cmd.solrDoc = doc;
            updateHandler.addDoc(cmd);

        } catch (UnsupportedOperationException | IOException e) {
            log.error("Adding the document to the index was not successful.");
            e.printStackTrace();
        }
    }

    /**
     * Handler which is called after every chunk of rows. When the chunk was processed the pending
     * changes are comitted to the solr index.
     * 
     */
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

    /**
     * Component destructor. Within the destructor the changes are comitted to the index and the
     * coreContainer is shutdown.
     * 
     */
    @PreDestroy
    public void release() {
        try {
            if (core != null) {
                core.getUpdateHandler().commit(new CommitUpdateCommand(solrQueryRequest, false));
                core.closeSearcher();
                core.close();
            }
            service.shutdownCoreContainer(this.solrHome);
        } catch (IOException e) {
            log.error("Committing changes & closiong the SolrCore was not successful.");
            e.printStackTrace();
        }
    }
}
