package com.wasif.imageupload.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private Path filesStoragePath;

    public ImageServiceImpl(@Value("${files.storage.location}") String filesStorageLocation) {
        // Initialize files upload location from properties
        this.filesStoragePath = Paths.get(filesStorageLocation);
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(filesStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.filesStoragePath.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = filesStoragePath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(filesStoragePath.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.filesStoragePath, 1).filter(path -> !path.equals(this.filesStoragePath))
                    .map(this.filesStoragePath::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    // method for job parameter and jon launcher
    @Override
    public void imageFiles(MultipartFile[] file) {
        // JobParameters jobParameters = new JobParametersBuilder()
        // .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        // try {
        // this.files = Arrays.asList(file);
        // jobLauncher.run(job, jobParameters);
        // } catch (JobExecutionAlreadyRunningException | JobRestartException |
        // JobInstanceAlreadyCompleteException
        // | JobParametersInvalidException e) {
        // e.printStackTrace();
        // }
    }

    // @Override
    // public List<MultipartFile> getFiles() {
    // return this.files;
    // }

    // @Override
    // public void setFiles(List<MultipartFile> file) {
    // this.files = file;
    // }

}
