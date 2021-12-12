package com.inho.mvc2.web.controller.fileupload.spring.domain;

import lombok.Data;

import java.util.List;

@Data
public class FileItem {
    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
