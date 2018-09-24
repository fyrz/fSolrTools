package com.fyr.talend.components.output;

import java.io.File;

import com.fyr.util.TemporaryFolderExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.talend.sdk.component.junit.ComponentsHandler;
import org.talend.sdk.component.junit5.Injected;
import org.talend.sdk.component.junit5.WithComponents;
import org.talend.sdk.component.runtime.output.Processor;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@WithComponents("com.fyr.talend.components")
public class SolrIndexerOutputTest {
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
    public void testSolrIndexer() throws ZipException {
        copyConfigToTemporaryFolder();
        SolrIndexerOutputConfiguration solrIndexerOutputConfiguration = new SolrIndexerOutputConfiguration()
                .setAppendIndex(true).setSolrCoreName("SolrHome")
                .setSolrHomePath(new File(temporaryFolder.getRoot(), "config").getAbsolutePath());
        Processor processor = handler.createProcessor(SolrIndexerOutput.class, solrIndexerOutputConfiguration);
        processor.start();
    }
}