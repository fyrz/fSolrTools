package com.fyr.talend.components.output;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

/**
 * Configuration of SolrIndexOptimizerOutput.
 */
@GridLayout({@GridLayout.Row({"solrCoreName"}), @GridLayout.Row({"solrHomePath"}),
        @GridLayout.Row({"appendIndex"}), @GridLayout.Row({"maxNumberOfSegments"}),
        @GridLayout.Row({"expungeDeletes"}),})
@Documentation("Configuration of SolrIndexOptimizerOutput.")
public class SolrIndexOptimizerOutputConfiguration implements Serializable {

    // auto generated serial version uid
    private static final long serialVersionUID = 3965563986672690804L;

    // Solr core name
    @Option
    @Documentation("Describes the solrCore name which shall be used by the component.")
    private String solrCoreName;
    // Boolean value which indicates if the index is in append mode.
    @Option
    @Documentation("This parameter controls if the index will be deleted or appended.")
    private boolean appendIndex = true;
    // SolrHome Path
    @Option
    @Documentation("Describes the path to solrHome this is also considered to be the rootPath for all configuration.")
    private String solrHomePath;

    // Maximum number of segments after optimize
    @Option
    @Documentation("Number of segments which remain after optimizing. This is affected by Integer.MAX_SIZE. So if one segment is bigger than Integer.MAX_SIZe than more than a single segment will exist.")
    private int maxNumberOfSegments = 1;

    // Flag which indicates if deletes are expunged or not
    @Option
    @Documentation("Describes if deletes are expunged while optimizing.")
    private boolean expungeDeletes = true;

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
     * @return SolrIndexOptimizerOutputConfiguration
     */
    public SolrIndexOptimizerOutputConfiguration setSolrCoreName(String solrCoreName) {
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
     * @return SolrIndexOptimizerOutputConfiguration
     */
    public SolrIndexOptimizerOutputConfiguration setAppendIndex(boolean appendIndex) {
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
     * @return SolrIndexOptimizerOutputConfiguration
     */
    public SolrIndexOptimizerOutputConfiguration setSolrHomePath(String solrHomePath) {
        this.solrHomePath = solrHomePath;
        return this;
    }

    /**
     * Returns the max amount of segments setting.
     * 
     * @return int
     */
    public int getMaxNumberOfSegments() {
        return maxNumberOfSegments;
    }

    /**
     * Sets the maximum amount of segments after optimization. This is the number of segments which
     * shall remain after optimization. This is not guaranteed as there is a segment size limit of
     * max int.
     * 
     * @param maxNumberOfSegments int
     * @return SolrIndexOptimizerOutputConfiguration
     */
    public SolrIndexOptimizerOutputConfiguration setMaxNumberOfSegments(int maxNumberOfSegments) {
        this.maxNumberOfSegments = maxNumberOfSegments;
        return this;
    }

    /**
     * Returns the setting if deletes are going to be expunged while optimizing.
     * 
     * @return boolean
     */
    public boolean getExpungeDeletes() {
        return expungeDeletes;
    }

    /**
     * Sets if deletes are going to be expunged while optimizing.
     * 
     * @param expungeDeletes boolean
     * @return SolrIndexOptimizerOutputConfiguration
     */
    public SolrIndexOptimizerOutputConfiguration setExpungeDeletes(boolean expungeDeletes) {
        this.expungeDeletes = expungeDeletes;
        return this;
    }
}
