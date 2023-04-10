package com.wasif.imageupload.message;

/*
 * Copyright (c) 2022 Md Wasif Ali.
 */

public class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
