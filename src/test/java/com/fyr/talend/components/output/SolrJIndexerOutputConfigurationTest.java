package com.fyr.talend.components.output;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SolrJIndexerOutputConfigurationTest {

    @Test
    public void testIndexerOutputConfiguration() {
        SolrJIndexerOutputConfiguration solrJIndexerOutputConfiguration = new SolrJIndexerOutputConfiguration();

        solrJIndexerOutputConfiguration.setAlwaysStreamDeletes(true);
        Assertions.assertEquals(true, solrJIndexerOutputConfiguration.getAlwaysStreamDeletes());
        solrJIndexerOutputConfiguration.setAlwaysStreamDeletes(false);
        Assertions.assertEquals(false, solrJIndexerOutputConfiguration.getAlwaysStreamDeletes());

        solrJIndexerOutputConfiguration.setClientMode(SolrJToolsClientMode.SIMPLE);
        Assertions.assertEquals(SolrJToolsClientMode.SIMPLE, solrJIndexerOutputConfiguration.getClientMode());

        solrJIndexerOutputConfiguration.setClientMode(SolrJToolsClientMode.CONCURRENT);
        Assertions.assertEquals(SolrJToolsClientMode.CONCURRENT, solrJIndexerOutputConfiguration.getClientMode());

        solrJIndexerOutputConfiguration.setThreadCount(1);
        Assertions.assertEquals(1, solrJIndexerOutputConfiguration.getThreadCount());
        solrJIndexerOutputConfiguration.setThreadCount(2);
        Assertions.assertEquals(2, solrJIndexerOutputConfiguration.getThreadCount());

        solrJIndexerOutputConfiguration.setConnectionTimeout(1000);
        Assertions.assertEquals(1000, solrJIndexerOutputConfiguration.getConnectionTimeout());
        solrJIndexerOutputConfiguration.setConnectionTimeout(2000);
        Assertions.assertEquals(2000, solrJIndexerOutputConfiguration.getConnectionTimeout());

        solrJIndexerOutputConfiguration.setQueueSize(400);
        Assertions.assertEquals(400, solrJIndexerOutputConfiguration.getQueueSize());
        solrJIndexerOutputConfiguration.setQueueSize(800);
        Assertions.assertEquals(800, solrJIndexerOutputConfiguration.getQueueSize());

        solrJIndexerOutputConfiguration.setSocketTimeout(100);
        Assertions.assertEquals(100, solrJIndexerOutputConfiguration.getSocketTimeout());
        solrJIndexerOutputConfiguration.setSocketTimeout(200);
        Assertions.assertEquals(200, solrJIndexerOutputConfiguration.getSocketTimeout());

        solrJIndexerOutputConfiguration.setSolrURL("http://test:8983/solr");
        Assertions.assertEquals("http://test:8983/solr", solrJIndexerOutputConfiguration.getSolrURL());
    }
}
