package com.wasif.imageupload.job;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import com.wasif.imageupload.service.ImageService;

@Configuration
public class JobWriter implements ItemWriter<MultipartFile> {
    @Autowired
    private ImageService imageService;

    @Override
    public void write(List<? extends MultipartFile> items) throws Exception {
        for (MultipartFile item : items) { // Write files to the local storage
            imageService.save(item);
        }
    }
}
