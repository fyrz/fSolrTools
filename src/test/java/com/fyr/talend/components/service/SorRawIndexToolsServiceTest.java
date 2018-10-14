package com.fyr.talend.components.service;

import com.fyr.util.TemporaryFolderExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.File;

public class SorRawIndexToolsServiceTest {

    @RegisterExtension
    public TemporaryFolderExtension temporaryFolder = new TemporaryFolderExtension();

    @Test
    public void testDeleteDirectory() {
        SolrRawIndexToolsService service = new SolrRawIndexToolsService();
        service.deleteDirectory(this.temporaryFolder.getRoot().toPath());

        // test that a wrong directory does not throw an exception
        service.deleteDirectory(new File(this.temporaryFolder.getRoot(), "xxxxxxx").toPath());
    }
}
