package com.fyr.talend.components.output;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.json.JsonObject;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.CommitUpdateCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.component.Icon.IconType;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.AfterGroup;
import org.talend.sdk.component.api.processor.BeforeGroup;
import org.talend.sdk.component.api.processor.ElementListener;
import org.talend.sdk.component.api.processor.Input;
import org.talend.sdk.component.api.processor.Processor;

import com.fyr.talend.components.service.FSolrToolsService;

@Version(1) // default version is 1, if some configuration changes happen between 2 versions
            // you can add a migrationHandler
@Icon(value = IconType.CUSTOM, custom = "nyhc")
@Processor(name = "SolrIndexOptimizer")
@Documentation("This component allows to optimize the Solr index. The component can be triggered by an arbitrary amount of rows. For each chunk of rows one optimize is triggered.")
public class SolrIndexOptimizerOutput implements Serializable {
    private static final long serialVersionUID = -1141250774595478552L;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final SolrIndexOptimizerOutputConfiguration configuration;
    private final FSolrToolsService service;

    private Path solrHome;
    private CoreContainer coreContainer;
    private SolrCore core;
    private boolean massOptimizeProtectionActive = false;

    private SolrQueryRequest solrQueryRequest;

    public SolrIndexOptimizerOutput(@Option("configuration") final SolrIndexOptimizerOutputConfiguration configuration,
            final FSolrToolsService service) {
        this.configuration = configuration;
        this.service = service;
    }

    @PostConstruct
    public void init() {
        solrHome = new File(configuration.getSolrHomePath()).toPath();

        coreContainer = service.initCore(solrHome, configuration.getSolrCoreName(), configuration.getAppendIndex());
        core = coreContainer.getCore(configuration.getSolrCoreName());
        log.info("SolrCore successfully retrieved.");

        ModifiableSolrParams params = new ModifiableSolrParams();
        solrQueryRequest = new LocalSolrQueryRequest(core, params);
    }

    @BeforeGroup
    public void beforeGroup() {
    }

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

    @AfterGroup
    public void afterGroup() {
        this.massOptimizeProtectionActive = false;
    }

    @PreDestroy
    public void release() {
        service.shutdownCoreContainer(this.solrHome);
    }
}