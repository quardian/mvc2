package com.inho.mvc2.web.controller.fileupload.servlet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
@RequestMapping("/fileupload/servelt/v2")
public class ServletUploadControllerV2
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


    @PostMapping("/upload")
    public String saveFile(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        for ( Part part : parts )
        {
            log.info("======= PART =======");
            log.info("name={}", part.getName() );
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {} {}", headerName, part.getHeader(headerName));
            }

            // 편의 메서드
            // content-disposition; filename
            //Content-Disposition: form-data; name="file"; filename="filename.png"
            //Content-Type: image/png
            log.info("submittedFilename={}", part.getSubmittedFileName() );
            log.info("size={}", part.getSize() ); //part body size

            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            // 파일에 저장하기
            String orgFilename = part.getSubmittedFileName();
            if ( StringUtils.hasText(orgFilename) ){
                String fullPath = fileUploadDir + orgFilename;
                log.info("파일저장 : fullPath={}", fullPath);

                part.write(fullPath);
            }
        }




        return "fileupload/upload-form";
    }

}
