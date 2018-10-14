package com.fyr.talend.components.output;

import com.fyr.talend.components.service.SolrRawIndexToolsService;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.CommitUpdateCommand;
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

@Version(1) // default version is 1, if some configuration changes happen between 2 versions
            // you can add a migrationHandler
@Icon(value = IconType.CUSTOM, custom = "nyhc")
@Processor(name = "IndexOptimizer")
@Documentation("This component allows to optimize the Solr index. The component can be triggered by an arbitrary amount of rows. For each chunk of rows one optimize is triggered.")
public class SolrIndexOptimizerOutput implements Serializable {

    // auto generated serial version uid
    private static final long serialVersionUID = -1141250774595478552L;

    // SLF4J logger
    protected final Logger log = LoggerFactory.getLogger(getClass());

    // Configuration
    private final SolrIndexOptimizerOutputConfiguration configuration;

    // Service
    private final SolrRawIndexToolsService service;

    // Path to solrHome directory
    private Path solrHome;

    // Solr CoreContainer
    private CoreContainer coreContainer;

    // SolrCore
    private SolrCore core;

    // Flag to protect that too many optimize requests are executed in a row.
    private boolean massOptimizeProtectionActive = false;

    // SolrQueryRequest
    private SolrQueryRequest solrQueryRequest;

    /**
     * SolrIndexOptimizerOutput CTor
     * 
     * @param configuration SolrIndexOptimizerOutputConfiguration
     * @param service       SolrRawIndexToolsService
     * 
     */
    public SolrIndexOptimizerOutput(
            @Option("configuration") final SolrIndexOptimizerOutputConfiguration configuration,
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
     * Row Handler which is called on each newly emitted row. In this routine the index is
     * optimized, but only once per chunk.
     * 
     * @param defaultInput javax.json.JsonObject
     */
    @ElementListener
    public void onNext(@Input final JsonObject defaultInput) {
        if (!this.massOptimizeProtectionActive) {
            try {
                CommitUpdateCommand cmd = new CommitUpdateCommand(solrQueryRequest, true);
                cmd.maxOptimizeSegments = configuration.getMaxNumberOfSegments();
                cmd.expungeDeletes = configuration.getExpungeDeletes();
                core.getUpdateHandler().commit(cmd);
                this.massOptimizeProtectionActive = true;
            } catch (IOException e) {
                log.error("Optimizing the index was not successful.");
                e.printStackTrace();
            }
        }

    }

    /**
     * Handler which is called after every chunk of rows. When the chunk was processed the
     * massOptimizeProtection is set to inactive. Which would lead to another optimize operation in
     * the next chunk.
     * 
     */
    @AfterGroup
    public void afterGroup() {
        this.massOptimizeProtectionActive = false;
    }

    /**
     * Component destructor. Within the destructor the changes are comitted to the index and the
     * coreContainer is shutdown.
     * 
     */
    @PreDestroy
    public void release() {
        service.shutdownCoreContainer(this.solrHome);
    }
}
