package com.fyr.talend.components.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.sdk.component.api.service.Service;

@Service
public class FSolrToolsService {

    // Initialize logger
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Delete path recursively 
     * 
     * @param path - path to be deleted
     */
    public void deleteDirectory(Path path) {
        try {
            Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
           log.error("Cleaning directory "+path.toString()+" was not successful.");
		}
    }

}