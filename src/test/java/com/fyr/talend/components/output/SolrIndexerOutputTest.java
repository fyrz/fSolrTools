package com.fyr.talend.components.output;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import com.fyr.util.TemporaryFolderExtension;
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
public class SolrIndexerOutputTest {
        @Injected
        private ComponentsHandler handler;

        @RegisterExtension
        public TemporaryFolderExtension temporaryFolder = new TemporaryFolderExtension();

        private void copyConfigToTemporaryFolder() throws ZipException {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(
                                classLoader.getResource("example_config/config.zip").getFile());
                ZipFile zipFile = new ZipFile(file.getAbsolutePath());
                zipFile.extractAll(this.temporaryFolder.getRoot().getAbsolutePath());
        }

        @Test
        public void testSolrIndexer() throws ZipException {
                copyConfigToTemporaryFolder();
                SolrIndexerOutputConfiguration solrIndexerOutputConfiguration =
                                new SolrIndexerOutputConfiguration().setAppendIndex(true)
                                                .setSolrCoreName("SolrCore")
                                                .setSolrHomePath(new File(temporaryFolder.getRoot(),
                                                                "config").getAbsolutePath());
                Processor processor = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor.start();

                Processor processor2 = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor2.start();

                processor.stop();
                processor2.stop();
        }

        @Test
        public void testDeleteIndexOnStartIndexer() throws ZipException {
                copyConfigToTemporaryFolder();
                SolrIndexerOutputConfiguration solrIndexerOutputConfiguration =
                                new SolrIndexerOutputConfiguration().setAppendIndex(false)
                                                .setSolrCoreName("SolrCore")
                                                .setSolrHomePath(new File(temporaryFolder.getRoot(),
                                                                "config").getAbsolutePath());
                Processor processor = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor.start();

                Processor processor2 = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor2.start();

                processor.stop();
                processor2.stop();
        }

        @Test
        public void overwriteOnStartIndexer() throws ZipException {
                copyConfigToTemporaryFolder();
                SolrIndexerOutputConfiguration solrIndexerOutputConfiguration =
                                new SolrIndexerOutputConfiguration().setAppendIndex(false)
                                                .setSolrCoreName("SolrCore")
                                                .setSolrHomePath(new File(temporaryFolder.getRoot(),
                                                                "config").getAbsolutePath());
                Processor processor = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor.start();
                processor.stop();

                Processor processor2 = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor2.start();
                processor2.stop();
        }

        @Test
        public void testBeforeAndAfterGroup() throws ZipException {
                copyConfigToTemporaryFolder();
                SolrIndexerOutputConfiguration solrIndexerOutputConfiguration =
                                new SolrIndexerOutputConfiguration().setAppendIndex(false)
                                                .setSolrCoreName("SolrCore")
                                                .setSolrHomePath(new File(temporaryFolder.getRoot(),
                                                                "config").getAbsolutePath());
                Processor processor = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor.start();
                processor.beforeGroup();
                processor.afterGroup(null);
                processor.stop();
        }

        @Test
        public void testWriteDocuments() throws ZipException {
                copyConfigToTemporaryFolder();
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                List<JsonObject> list = new ArrayList<JsonObject>();
                list.add(jsonObjectBuilder.add("id", "123").add("title_txt", "title123").build());
                list.add(jsonObjectBuilder.add("id", "124").add("title_txt", "title124").build());

                SolrIndexerOutputConfiguration solrIndexerOutputConfiguration =
                                new SolrIndexerOutputConfiguration().setAppendIndex(false)
                                                .setSolrCoreName("SolrCore")
                                                .setSolrHomePath(new File(temporaryFolder.getRoot(),
                                                                "config").getAbsolutePath());
                Processor processor = handler.createProcessor(SolrIndexerOutput.class,
                                solrIndexerOutputConfiguration);
                processor.start();

                handler.collect(processor, new JoinInputFactory().withInput("__default__", list));

                processor.stop();
        }
}
