package com.fyr.talend.components.output;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.fyr.util.TemporaryFolderExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.talend.sdk.component.junit.ComponentsHandler;
import org.talend.sdk.component.junit.JoinInputFactory;
import org.talend.sdk.component.junit5.Injected;
import org.talend.sdk.component.junit5.WithComponents;
import org.talend.sdk.component.runtime.output.Processor;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@WithComponents("com.fyr.talend.components")
public class SolrIndexOptimizerOutputTest {
    @Injected
    private ComponentsHandler handler;

    @RegisterExtension
    public TemporaryFolderExtension temporaryFolder = new TemporaryFolderExtension();

    private void copyConfigToTemporaryFolder() throws ZipException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("example_config/config.zip").getFile());
        ZipFile zipFile = new ZipFile(file.getAbsolutePath());
        zipFile.extractAll(this.temporaryFolder.getRoot().getAbsolutePath());
    }

    @Test
    public void testSolrIndexOptimizer() throws ZipException {
        copyConfigToTemporaryFolder();
        SolrIndexOptimizerOutputConfiguration solrIndexOptimizerOutputConfiguration = new SolrIndexOptimizerOutputConfiguration()
                .setAppendIndex(true).setExpungeDeletes(false).setMaxNumberOfSegments(2).setSolrCoreName("SolrCore")
                .setSolrHomePath(new File(temporaryFolder.getRoot(), "config").getAbsolutePath());

        Assertions.assertEquals(false, solrIndexOptimizerOutputConfiguration.getExpungeDeletes());
        solrIndexOptimizerOutputConfiguration.setExpungeDeletes(true);
        Assertions.assertEquals(true, solrIndexOptimizerOutputConfiguration.getExpungeDeletes());
        Assertions.assertEquals(2, solrIndexOptimizerOutputConfiguration.getMaxNumberOfSegments());
        solrIndexOptimizerOutputConfiguration.setMaxNumberOfSegments(1);
        Assertions.assertEquals(1, solrIndexOptimizerOutputConfiguration.getMaxNumberOfSegments());
        Processor processor = handler.createProcessor(SolrIndexOptimizerOutput.class,
                solrIndexOptimizerOutputConfiguration);
        processor.start();
        processor.stop();
    }

    @Test
    public void testBeforeAndAfterGroup() throws ZipException {
        copyConfigToTemporaryFolder();
        SolrIndexOptimizerOutputConfiguration solrIndexOptimizerOutputConfiguration = new SolrIndexOptimizerOutputConfiguration()
                .setAppendIndex(true).setExpungeDeletes(false).setMaxNumberOfSegments(2).setSolrCoreName("SolrCore")
                .setSolrHomePath(new File(temporaryFolder.getRoot(), "config").getAbsolutePath());
        Processor processor = handler.createProcessor(SolrIndexOptimizerOutput.class,
                solrIndexOptimizerOutputConfiguration);
        processor.start();
        processor.beforeGroup();
        processor.afterGroup(null);
        processor.stop();
    }

    @Test
    public void testOptimize() throws ZipException {
        copyConfigToTemporaryFolder();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        List<JsonObject> list = new ArrayList<JsonObject>();
        list.add(jsonObjectBuilder.add("id", "1").build());

        SolrIndexOptimizerOutputConfiguration solrIndexOptimizerOutputConfiguration = new SolrIndexOptimizerOutputConfiguration()
                .setAppendIndex(true).setExpungeDeletes(false).setMaxNumberOfSegments(2).setSolrCoreName("SolrCore")
                .setSolrHomePath(new File(temporaryFolder.getRoot(), "config").getAbsolutePath());
        Processor processor = handler.createProcessor(SolrIndexOptimizerOutput.class,
                solrIndexOptimizerOutputConfiguration);
        processor.start();
        handler.collect(processor, new JoinInputFactory().withInput("__default__", list));
        processor.stop();
    }
}