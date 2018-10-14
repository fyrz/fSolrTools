package com.fyr.talend.components.service;

import com.fyr.talend.components.output.SolrJToolsClientMode;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.sdk.component.api.service.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class which provides general SolrJ functionality for the component suite.
 */
@Service
public class SolrJToolsService {

    // Initialize logger
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private Map<String, SolrClient> solrClients = new HashMap<>();

    public SolrClient retrieveSolrClient(SolrJToolsClientMode mode, final String solrUrl, final int connectionTimeout, final int socketTimeout, final boolean alwaysStreamDeletes, final int queueSize, final int threadCount) {
        initSolrClient(mode, solrUrl, connectionTimeout, socketTimeout, alwaysStreamDeletes, queueSize, threadCount);
        return solrClients.get(solrUrl);
    }

    private void initSolrClient(SolrJToolsClientMode mode, final String solrUrl, final int connectionTimeout, final int socketTimeout, final boolean alwaysStreamDeletes, final int queueSize, final int threadCount) {
        SolrClient solrClient = null;
        if (!solrClients.containsKey(solrUrl)) {
            switch (mode) {
                case CONCURRENT:
                    if (alwaysStreamDeletes) {
                        solrClient = new ConcurrentUpdateSolrClient.Builder(solrUrl).withConnectionTimeout(connectionTimeout).withSocketTimeout(socketTimeout).withQueueSize(queueSize).withThreadCount(threadCount).alwaysStreamDeletes().build();
                    } else {
                        solrClient = new ConcurrentUpdateSolrClient.Builder(solrUrl).withConnectionTimeout(connectionTimeout).withSocketTimeout(socketTimeout).withQueueSize(queueSize).withThreadCount(threadCount).neverStreamDeletes().build();
                    }
                    break;
                case SIMPLE:
                    solrClient = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(connectionTimeout).withSocketTimeout(socketTimeout).build();
                    break;
            }
            solrClients.put(solrUrl, solrClient);
        }
    }

}
