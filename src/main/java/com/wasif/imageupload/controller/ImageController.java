package com.wasif.imageupload.controller;

/*
 * Copyright (c) 2023 Md Wasif Ali.
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.SizeLimitExceededException;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.wasif.imageupload.exception.StorageException;
import com.wasif.imageupload.fileManager.FileCache;
import com.wasif.imageupload.job.ImageJobLauncher;
import com.wasif.imageupload.message.ResponseMessage;
import com.wasif.imageupload.model.FileInfo;
import com.wasif.imageupload.service.ImageService;

/**
 * This is a controller class of File Handling.
 * It processes multipart images.
 *
 * @author Md Wasif Ali
 * @version 1.0
 * @since 10/04/23
 */
@Controller
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    private ImageJobLauncher imageJobLauncher;

    @Autowired
    private FileCache fieCache;

    /**
     * upload bulk image using Multipart functionality
     *
     * @param files
     * @return message
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {
                imageService.save(file);
                fileNames.add(file.getOriginalFilename());
            });

            message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Fail to upload files!";
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /**
     * bulk image upload with batch
     * 
     * @param files
     * @return message
     */
    @PostMapping("/image-upload")
    public String imageUpload(@RequestParam("files") MultipartFile[] files)
            throws StorageException, MultipartException, SizeLimitExceededException,
            JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        long reqId = System.currentTimeMillis();
        fieCache.setFiles(reqId, new ArrayList<>(Arrays.asList(files)));
        imageJobLauncher.startFilesJob(reqId);

        return "File uploaded successfully.";
    }

    /**
     * get a list of uploaded file
     *
     * @return list of file information
     */
    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = imageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ImageController.class, "getFile", path.getFileName().toString()).build()
                    .toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    /**
     * download file
     *
     * @param filename
     * @return file
     */
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = imageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    /***
     * 
     * Exception handlers for
     * various storage
     * and file exceptions.
     */

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException ex) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException ex) {
        return new ResponseEntity<>("No valid file found with request.", HttpStatus.BAD_REQUEST);
    }
}