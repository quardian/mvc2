package com.inho.mvc2.web.controller.fileupload.spring;

import com.inho.mvc2.web.controller.fileupload.spring.domain.FileItem;
import com.inho.mvc2.web.controller.fileupload.spring.domain.FileItemRepository;
import com.inho.mvc2.web.controller.fileupload.spring.domain.FileStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ImagesController {
    private final FileItemRepository fileItemRepository;
    private final FileStoreService fileStoreService;

    @ResponseBody
    @GetMapping(value="/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        // file:/user/filename.jpg
        return new UrlResource("file:" + fileStoreService.getFullPath(filename));
    }


    @GetMapping(value="/attach/{id}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long id) throws MalformedURLException {
        // file:/user/filename.jpg
        FileItem item = fileItemRepository.findById(id);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource urlResource = new UrlResource("file:" + fileStoreService.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
                .body(urlResource);
    }
}
