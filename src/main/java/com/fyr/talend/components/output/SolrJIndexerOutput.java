package com.fyr.talend.components.output;

import com.fyr.talend.components.helper.JsonProcessingHelper;
import com.fyr.talend.components.service.SolrJToolsService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.Serializable;

/**
 * This component writes to a Solr instance using SolrJ and Generic Row input.
 */
@Version(1) // default version is 1, if some configuration changes happen between 2 versions
// you can add a migrationHandler
// @Icon(Icon.IconType.STAR)
@Icon(value = Icon.IconType.CUSTOM, custom = "nyhc")
@Processor(name = "JIndexer")
@Documentation("This component writes to a Solr instance using SolrJ and Generic Row input.")
public class SolrJIndexerOutput implements Serializable {

    // auto generated serial version uid
    private static final long serialVersionUID = -1835083220459563930L;

    // SLF4J logger
    protected final Logger log = LoggerFactory.getLogger(getClass());

    // Configuration
    private final SolrJIndexerOutputConfiguration configuration;

    // Service
    private final SolrJToolsService service;

    // SolrJClient
    private SolrClient solrClient = null;

    /**
     * SolrIndexerOutput CTor
     *
     * @param configuration SolrIndexerOutputConfiguration
     * @param service       SolrRawIndexToolsService
     */
    public SolrJIndexerOutput(
            @Option("configuration") final SolrJIndexerOutputConfiguration configuration,
            final SolrJToolsService service) {
        this.configuration = configuration;
        this.service = service;

        SolrJToolsClientMode solrJToolsServiceMode = this.configuration.getClientMode();
        this.solrClient = this.service.retrieveSolrClient(solrJToolsServiceMode, this.configuration.getSolrURL(), this.configuration.getConnectionTimeout(), this.configuration.getSocketTimeout(), this.configuration.getAlwaysStreamDeletes(), this.configuration.getQueueSize(), this.configuration.getThreadCount());
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
    }

    /**
     *
     */
    @BeforeGroup
    public void beforeGroup() {
        // no action before a chunk is processed.
    }

    /**
     * @param defaultInput javax.json.JsonObject
     */
    @ElementListener
    public void onNext(@Input final JsonObject defaultInput) throws IOException, SolrServerException {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        // Iterating over the entries and add them into the document
        for (String key : defaultInput.keySet()) {
            Object obj = JsonProcessingHelper.getJavaTypeFromJsonJsonValue(defaultInput.get(key));
            if (obj != null) {
                solrInputDocument.addField(key, obj);
            }
        }
        solrClient.add(solrInputDocument);
    }

    /**
     *
     */
    @AfterGroup
    public void afterGroup() throws IOException, SolrServerException {
        solrClient.commit(true, true);
    }

    /**
     *
     */
    @PreDestroy
    public void release() throws IOException, SolrServerException {
        solrClient.commit(true, true);
    }
}
