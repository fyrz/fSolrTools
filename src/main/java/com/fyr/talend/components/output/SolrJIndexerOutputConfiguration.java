package com.fyr.talend.components.output;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.constraint.Required;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

/**
 * Configuration of SolrJIndexerOutputConfiguration.
 */
@GridLayout({
        @GridLayout.Row({"solrURL"}),
        @GridLayout.Row({"clientMode"}),
        @GridLayout.Row({"connectionTimeout"}),
        @GridLayout.Row({"socketTimeout"}),
        @GridLayout.Row({"alwaysStreamDeletes"}),
        @GridLayout.Row({"queueSize"}),
        @GridLayout.Row({"threadCount"}),
})
@Documentation("Configuration of SolrJIndexerOutputConfiguration.")
public class SolrJIndexerOutputConfiguration implements Serializable {

    // Solr client mode
    @Option
    @Required
    @Documentation("Describes the client mode used.")
    SolrJToolsClientMode clientMode = SolrJToolsClientMode.SIMPLE;

    // Solr url
    @Option
    @Required
    @Documentation("Describes the solrUrl used by the component.")
    private String solrURL;

    // Connection timeout
    @Option
    @Documentation("Connection timeout.")
    private int connectionTimeout = 10000;
    // Socket timeout
    @Option
    @Documentation("Socket timeout.")
    private int socketTimeout = 60000;
    // Stream deletes (Concurrent)
    @Option
    @Documentation("Always Stream deletes ?")
    private Boolean alwaysStreamDeletes = false;
    // Queue size (Concurrent)
    @Option
    @Documentation("Amount of documents queued before transmitted.")
    private int queueSize = 10;
    // Thread count (Concurrent)
    @Option
    @Documentation("Amount of threads used for communication to Solr.")
    private int threadCount = 1;

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public SolrJIndexerOutputConfiguration setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public SolrJIndexerOutputConfiguration setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public Boolean getAlwaysStreamDeletes() {
        return alwaysStreamDeletes;
    }

    public SolrJIndexerOutputConfiguration setAlwaysStreamDeletes(Boolean alwaysStreamDeletes) {
        this.alwaysStreamDeletes = alwaysStreamDeletes;
        return this;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public SolrJIndexerOutputConfiguration setQueueSize(int queueSize) {
        this.queueSize = queueSize;
        return this;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public SolrJIndexerOutputConfiguration setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    public SolrJToolsClientMode getClientMode() {
        return clientMode;
    }

    public SolrJIndexerOutputConfiguration setClientMode(SolrJToolsClientMode clientMode) {
        this.clientMode = clientMode;
        return this;
    }

    public String getSolrURL() {
        return solrURL;
    }

    public SolrJIndexerOutputConfiguration setSolrURL(String solrURL) {
        this.solrURL = solrURL;
        return this;
    }
}
