package com.inho.mvc2.web.controller.fileupload.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequiredArgsConstructor


/**

 */
@RequestMapping("/fileupload/spring")
public class SpringUploadController
{
    /**
     * apllication.properties에 있는 값을 주입 받는다.
     * @return
     */
    @Value("${file.upload.dir}")
    private String fileUploadDir;


    @GetMapping("/upload")
    public String newFile()
    {
        return "fileupload/upload-form";
    }

    /*
        @ModelAttribute에서도 MultipartFile 필드를 적용할 수 있다.
        ArgumentResolver가 다 알아서 처리해 줌.
     */

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file,
                           HttpServletRequest request) throws ServletException, IOException {

        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("file={}", file);

        if (! file.isEmpty() ){
            String orgFilename = file.getOriginalFilename();
            String fullPath = fileUploadDir + orgFilename;

            log.info("파일 저장 fullpath={}", fullPath);
            file.transferTo( new File(fullPath) );
        }

        return "fileupload/upload-form";
    }

}
