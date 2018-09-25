package com.fyr.talend.components.service;

import java.io.File;

import com.fyr.util.TemporaryFolderExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class FSolrToolsServiceTest {

    @RegisterExtension
    public TemporaryFolderExtension temporaryFolder = new TemporaryFolderExtension();

    @Test
    public void testDeleteDirectory(){
        FSolrToolsService service = new FSolrToolsService();
        service.deleteDirectory(this.temporaryFolder.getRoot().toPath());

        // test that a wrong directory does not throw an exception
        service.deleteDirectory(new File(this.temporaryFolder.getRoot(), "xxxxxxx").toPath());
    }
}