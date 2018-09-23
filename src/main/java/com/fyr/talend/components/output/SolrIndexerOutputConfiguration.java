package com.fyr.talend.components.output;

import java.io.Serializable;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
    // the generated layout put one configuration entry per line,
    // customize it as much as needed
    @GridLayout.Row({ "solrCoreName" }),
    @GridLayout.Row({ "solrHomePath" }),
    @GridLayout.Row({ "appendIndex" }),
})
@Documentation("TODO fill the documentation for this configuration")
public class SolrIndexerOutputConfiguration implements Serializable {
    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private String solrCoreName;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private boolean appendIndex;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
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
