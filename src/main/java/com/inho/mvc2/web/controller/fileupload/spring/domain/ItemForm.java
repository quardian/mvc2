package com.inho.mvc2.web.controller.fileupload.spring.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {
    private Long id;
    private String itemName;
    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;
}
