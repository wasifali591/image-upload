package com.wasif.imageupload.fileManager;

/*
 * Copyright (c) 2022 Md Wasif Ali.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * in thhis calss we are making a Map with the Multipart files we are getting from request param
 *
 * @author Md Wasif Ali
 * @version 1.0
 * @since 10/04/23
 */
@Component
public class FileCache {
    private static Map<Long, ArrayList<MultipartFile>> filesCache = new HashMap<>();

    /**
     * put Multipart file into a Map
     *
     * @param id
     * @param files
     */
    public void setFiles(Long id, ArrayList<MultipartFile> files) {
        filesCache.put(id, files);
    }

    /**
     * read each file
     *
     * @param id
     * @return file
     */
    public MultipartFile getFile(Long id) {
        // Check if the cache contains the file
        if (filesCache.size() > 0 && filesCache.containsKey(id)) {
            ArrayList<MultipartFile> files = filesCache.get(id);
            if (files.size() > 0) {
                // Get file from cache and remove file
                MultipartFile file = files.get(0);
                files.remove(file);
                filesCache.put(id, files);
                if (files.size() == 0) {
                    // Clear the cache at end of files
                    filesCache.remove(id);
                }
                return file;
            }
        }
        return null;
    }
}
