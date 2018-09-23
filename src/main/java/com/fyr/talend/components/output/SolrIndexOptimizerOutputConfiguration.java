package com.fyr.talend.components.output;

import java.io.Serializable;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({ 
    @GridLayout.Row({ "solrCoreName" }), 
    @GridLayout.Row({ "solrHomePath" }),
    @GridLayout.Row({ "appendIndex" }), 
    @GridLayout.Row({ "maxNumberOfSegments" }),
    @GridLayout.Row({ "expungeDeletes" }), 
})
@Documentation("TODO fill the documentation for this configuration")
public class SolrIndexOptimizerOutputConfiguration implements Serializable {

    private static final long serialVersionUID = 3965563986672690804L;

    @Option
    @Documentation("Describes the solrCore name which shall be used by the component.")
    private String solrCoreName;

    @Option
    @Documentation("This parameter controls if the index will be deleted or appended.")
    private boolean appendIndex = true;

    @Option
    @Documentation("Describes the path to solrHome this is also considered to be the rootPath for all configuration.")
    private String solrHomePath;

    @Option
    @Documentation("Number of segments which remain after optimizing. This is affected by Integer.MAX_SIZE. So if one segment is bigger than Integer.MAX_SIZe than more than a single segment will exist.")
    private int maxNumberOfSegments = 1;

    @Option
    @Documentation("Describes if deletes are expunged while optimizing.")
    private boolean expungeDeletes = true;

    public String getSolrCoreName() {
        return solrCoreName;
    }

    public SolrIndexOptimizerOutputConfiguration setSolrCoreName(String solrCoreName) {
        this.solrCoreName = solrCoreName;
        return this;
    }

    public boolean getAppendIndex() {
        return appendIndex;
    }

    public SolrIndexOptimizerOutputConfiguration setAppendIndex(boolean appendIndex) {
        this.appendIndex = appendIndex;
        return this;
    }

    public String getSolrHomePath() {
        return solrHomePath;
    }

    public SolrIndexOptimizerOutputConfiguration setSolrHomePath(String solrHomePath) {
        this.solrHomePath = solrHomePath;
        return this;
    }

    public int getMaxNumberOfSegments() {
        return maxNumberOfSegments;
    }

    public SolrIndexOptimizerOutputConfiguration setMaxNumberOfSegments(int maxNumberOfSegments) {
        this.maxNumberOfSegments = maxNumberOfSegments;
        return this;
    }

    public boolean getExpungeDeletes() {
        return expungeDeletes;
    }

    public SolrIndexOptimizerOutputConfiguration setExpungeDeletes(boolean expungeDeletes) {
        this.expungeDeletes = expungeDeletes;
        return this;
    }
}