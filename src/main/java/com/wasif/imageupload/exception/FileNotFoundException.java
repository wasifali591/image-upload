package com.wasif.imageupload.exception;

/*
 * Copyright (c) 2022 Md Wasif Ali.
 */

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
