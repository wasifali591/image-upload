package com.wasif.imageupload.job;

import java.text.ParseException;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.wasif.imageupload.fileManager.FileCache;

@Configuration
@StepScope
public class ImageJobReader implements ItemReader<MultipartFile> {

    @Autowired
    public FileCache fileCacheReader;

    @Value("#{jobParameters['REQ_ID']}")
    private long reqId;

    @Override
    @Nullable
    public MultipartFile read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        MultipartFile file = fileCacheReader.getFile(reqId);
        if (file != null) { // Indicate the file to the reader
            return file;
        }
        return null; // Indicate the EOF to the reader
    }
}