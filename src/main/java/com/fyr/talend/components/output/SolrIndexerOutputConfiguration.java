package com.fyr.talend.components.output;

import java.io.Serializable;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
    @GridLayout.Row({ "solrCoreName" }),
    @GridLayout.Row({ "solrHomePath" }),
    @GridLayout.Row({ "appendIndex" }),
})

@Documentation("Configuration for the SolrIndexer.")
public class SolrIndexerOutputConfiguration implements Serializable {
    private static final long serialVersionUID = -2561434401619643146L;

    @Option
    @Documentation("Describes the solrCore name which shall be used by the component.")
    private String solrCoreName;

    @Option
    @Documentation("Describes if the index will be deleted or appended.")
    private boolean appendIndex;

    @Option
    @Documentation("Describes the path to solrHome this is also considered to be the rootPath for all configuration.")
    private String solrHomePath;

    public String getSolrCoreName() {
        return solrCoreName;
    }

    public SolrIndexerOutputConfiguration setSolrCoreName(String solrCoreName) {
        this.solrCoreName = solrCoreName;
        return this;
    }

    public boolean getAppendIndex() {
        return appendIndex;
    }

    public SolrIndexerOutputConfiguration setAppendIndex(boolean appendIndex) {
        this.appendIndex = appendIndex;
        return this;
    }

    public String getSolrHomePath() {
        return solrHomePath;
    }

    public SolrIndexerOutputConfiguration setSolrHomePath(String solrHomePath) {
        this.solrHomePath = solrHomePath;
        return this;
    }
}
