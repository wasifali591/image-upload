package com.wasif.imageupload.exception;

/*
 * Copyright (c) 2022 Md Wasif Ali.
 */

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
