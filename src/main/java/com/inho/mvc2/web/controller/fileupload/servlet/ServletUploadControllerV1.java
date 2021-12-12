package com.inho.mvc2.web.controller.fileupload.servlet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequiredArgsConstructor


/**
 * 멀티파트 사용 옵션
 *
 * application.properties
 * #업로드 사이즈 제한
 * spring.servlet.multipart.max-file-size=1MB
 * spring.servlet.multipart.max-request-size=10MB
 *
 * #멀티파트 관련 처리 할지 여부 : false 인데, multipart
 * spring.servlet.multipart.enabled=false
 */
@RequestMapping("/fileupload/servelt/v1")
public class ServletUploadControllerV1
{

    @GetMapping("/upload")
    public String newFile()
    {
        return "fileupload/upload-form";
    }


    @PostMapping("/upload")
    public String saveFile(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        return "fileupload/upload-form";
    }

}
