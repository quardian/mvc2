package com.inho.mvc2.web.controller.fileupload.spring.domain;

import lombok.Data;

import java.beans.Transient;
import java.io.IOException;

@Data
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
    private long  fileSize;
    private String mimeType;
    private int imgWidth;
    private int imgHeight;

    private IOException error;
    public boolean isOK() { return fileSize > 0L; }
}


