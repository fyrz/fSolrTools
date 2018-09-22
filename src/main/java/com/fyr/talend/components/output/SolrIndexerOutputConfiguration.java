package com.fyr.talend.components.output;

import java.io.Serializable;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
    // the generated layout put one configuration entry per line,
    // customize it as much as needed
    @GridLayout.Row({ "indexPath" }),
    @GridLayout.Row({ "appendIndex" }),
    @GridLayout.Row({ "schemaPath" })
})
@Documentation("TODO fill the documentation for this configuration")
public class SolrIndexerOutputConfiguration implements Serializable {
    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private String indexPath;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private boolean appendIndex;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private String schemaPath;

    public String getIndexPath() {
        return indexPath;
    }

    public SolrIndexerOutputConfiguration setIndexPath(String indexPath) {
        this.indexPath = indexPath;
        return this;
    }

    public boolean getAppendIndex() {
        return appendIndex;
    }

    public SolrIndexerOutputConfiguration setAppendIndex(boolean appendIndex) {
        this.appendIndex = appendIndex;
        return this;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public SolrIndexerOutputConfiguration setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
        return this;
    }
}