package com.inho.mvc2.web.controller.fileupload.spring.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:default")
class FileStoreServiceTest {

    @Autowired
    FileUploadProperties fileUploadProperties;


    @Test
    void fileStore()
    {
        FileStoreService fileStoreService = new FileStoreService(fileUploadProperties);

        String fullPath = fileStoreService.getFullPath("fileName.jpg");
        Assertions.assertThat(fullPath).isEqualTo("D:\\home\\upload\\fileName.jpg");

    }
}