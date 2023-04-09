package com.wasif.imageupload.fileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileCache {
    private static Map<Long, ArrayList<MultipartFile>> filesCache = new HashMap<>();

    public void setFiles(Long id, ArrayList<MultipartFile> files) {
        filesCache.put(id, files);
    }

    public MultipartFile getFile(Long id) {
        // Check if the cache contains the file
        if (filesCache.size() > 0 && filesCache.containsKey(id)) {
            ArrayList<MultipartFile> files = filesCache.get(id);
            if (files.size() > 0) {
                // Get file from cache and remove file
                MultipartFile file = files.get(0);
                files.remove(file);
                filesCache.put(id, files);
                if (files.size() == 0) { // Clear the cache at end of files
                    filesCache.remove(id);
                }
                return file;
            }
        }
        return null;
    }
}
