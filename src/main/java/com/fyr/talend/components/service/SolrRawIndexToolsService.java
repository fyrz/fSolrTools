package com.fyr.talend.components.service;

import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.sdk.component.api.service.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class which provides general functionality for the component suite. Such as the
 * initializer of the CoreContainer and the management of CoreContainer handles.
 *
 */
@Service
public class SolrRawIndexToolsService {

    // Initialize logger
    protected final Logger log = LoggerFactory.getLogger(getClass());

    // Map of CoreContainer handles.
    private Map<String, CoreContainer> coreContainers = new HashMap<String, CoreContainer>();

    // Constant which is a relative path within solrHome pointing to the index
    // directory.
    private final String solrDataSubFolder = "data";

    /**
     * Delete path recursively
     * 
     * @param path Path to be deleted
     */
    public void deleteDirectory(Path path) {
        try {
            Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            log.error("Cleaning directory " + path.toString() + " was not successful.");
        }
    }

    /**
     * Init CoreContainer so that it can be reused accross the different components without locking
     * issues.
     * 
     * There can be only one CoreContainer per solrHomePath. If a CoreContainer shall be initalized
     * for an existing solrHomePath the already initialized handle is returned.
     * 
     * @param solrHomePath Path to solrHome this is also the index for the different coreContainers
     * @param appendIndex  Boolean value which indicates wether an index is newly created or
     *                     appended. Existing indexes will be deleted if appendIndex is false.
     * @return
     */
    public CoreContainer initCore(final Path solrHomePath, final String coreName,
            boolean appendIndex) {
        CoreContainer coreContainer = null;
        final String coreIdentifier = solrHomePath.toAbsolutePath().toString();

        if (coreContainers.containsKey(coreIdentifier)) {
            if (!appendIndex) {
                log.warn("AppendIndex setting is ignored whenever an active corecontainer exists.");
            }
            coreContainer = coreContainers.get(coreIdentifier);
        } else {
            Path solrIndexPath =
                    new File(new File(solrHomePath.toFile(), coreName), solrDataSubFolder).toPath();
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
     * @param solrHomePath Path to solrHome which is the identifier for the coreContainers
     */
    public synchronized void shutdownCoreContainer(Path solrHomePath) {
        final String coreIdentifier = solrHomePath.toAbsolutePath().toString();
        if (coreContainers.containsKey(coreIdentifier)) {
            coreContainers.get(coreIdentifier).shutdown();
            coreContainers.remove(coreIdentifier);
        }
    }

}
