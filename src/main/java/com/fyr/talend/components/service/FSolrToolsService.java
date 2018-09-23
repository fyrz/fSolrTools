package com.fyr.talend.components.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.sdk.component.api.service.Service;

@Service
public class FSolrToolsService {

    // Initialize logger
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private Map<String, CoreContainer> coreContainers = new HashMap<String, CoreContainer>();

    private final String solrDataSubFolder = "data";

    /**
     * Delete path recursively
     * 
     * @param path Path to be deleted
     */
    public void deleteDirectory(Path path) {
        try {
            Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            log.error("Cleaning directory " + path.toString() + " was not successful.");
        }
    }

    /**
     * Init CoreContainer. So that it can be reused accross the different components
     * without locking issues.
     * 
     * @param solrHomePath Path to solrHome this is also the index for the different
     *                     coreContainers
     * @param appendIndex  Boolean value which indicates wether an index is newly
     *                     created or appended. Existing indexes will be deleted if
     *                     appendIndex is false.
     * @return
     */
    public CoreContainer initCore(final Path solrHomePath, final String coreName, boolean appendIndex) {
        CoreContainer coreContainer = null;
        final String coreIdentifier = solrHomePath.toAbsolutePath().toString();

        if (coreContainers.containsKey(coreIdentifier)) {
            coreContainer = coreContainers.get(coreIdentifier);
        } else {
            Path solrIndexPath = new File(new File(solrHomePath.toFile(), coreName), solrDataSubFolder).toPath();
            if (!appendIndex) {
                deleteDirectory(solrIndexPath);
            }
            coreContainer = new CoreContainer(solrHomePath.toAbsolutePath().toString());
            coreContainer.load();
            coreContainers.put(coreIdentifier, coreContainer);
            log.info("CoreContainer successfully initialized.");
        }
        return coreContainer;
    }

    /**
     * Shutdown coreContainer
     * 
     * @param solrHomePath Path to solrHome which is the identifier for the
     *                     coreContainers
     */
    public synchronized void shutdownCoreContainer(Path solrHomePath) {
        final String coreIdentifier = solrHomePath.toAbsolutePath().toString();
        if (coreContainers.containsKey(coreIdentifier)) {
            coreContainers.get(coreIdentifier).shutdown();
            coreContainers.remove(coreIdentifier);
        }
    }

}