package com.wasif.imageupload.job;

/*
 * Copyright (c) 2022 Md Wasif Ali.
 */

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Configuration
public class ImageJobProcessor implements ItemProcessor<MultipartFile, MultipartFile> {

    @Override
    @Nullable
    public MultipartFile process(@NonNull MultipartFile item) throws Exception {
        return item;
    }

}
