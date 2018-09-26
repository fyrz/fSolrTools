package com.fyr.talend.components.output;

import java.io.Serializable;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

/**
 * Configuration of SolrIndexerOutput.
 * 
 */
@GridLayout({@GridLayout.Row({"solrCoreName"}), @GridLayout.Row({"solrHomePath"}),
        @GridLayout.Row({"appendIndex"}),})
@Documentation("Configuration for the SolrIndexer.")
public class SolrIndexerOutputConfiguration implements Serializable {

    // auto generated serial version uid
    private static final long serialVersionUID = -2561434401619643146L;

    // Solr core name
    @Option
    @Documentation("Describes the solrCore name which shall be used by the component.")
    private String solrCoreName;

    // Boolean value which indicates if the index is in append mode.
    @Option
    @Documentation("Describes if the index will be deleted or appended.")
    private boolean appendIndex;

    // SolrHome Path
    @Option
    @Documentation("Describes the path to solrHome this is also considered to be the rootPath for all configuration.")
    private String solrHomePath;

    /**
     * Get Solr core name
     * 
     * @return String
     */
    public String getSolrCoreName() {
        return solrCoreName;
    }

    /**
     * Set the Solr core name to be used.
     * 
     * @param solrCoreName Name of the Solr core.
     * 
     * @return SolrIndexerOutputConfiguration
     */
    public SolrIndexerOutputConfiguration setSolrCoreName(String solrCoreName) {
        this.solrCoreName = solrCoreName;
        return this;
    }

    /**
     * Returns if the index will be appended.
     * 
     */
    public boolean getAppendIndex() {
        return appendIndex;
    }

    /**
     * Sets if the index is appended or deleted and newly created.
     * 
     * @param appendIndex if set to true the index is not deleted.
     * @return SolrIndexerOutputConfiguration
     */
    public SolrIndexerOutputConfiguration setAppendIndex(boolean appendIndex) {
        this.appendIndex = appendIndex;
        return this;
    }

    /**
     * Get SolrHomePath
     * 
     * @return Path to SolrHome
     */
    public String getSolrHomePath() {
        return solrHomePath;
    }

    /**
     * Set SolrHomePath
     * 
     * @param solrHomePath Path to SolrHome
     * @return SolrIndexerOutputConfiguration
     */
    public SolrIndexerOutputConfiguration setSolrHomePath(String solrHomePath) {
        this.solrHomePath = solrHomePath;
        return this;
    }
}
