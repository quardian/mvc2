package com.inho.mvc2.web.controller.fileupload.spring.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConfigurationProperties(prefix = "file.upload")
@Data
public class FileUploadProperties
{
    private String dir;
}
