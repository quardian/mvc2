package com.inho.mvc2.web.controller.fileupload.spring.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileStoreService {

    private final FileUploadProperties fileUploadProperties;
    private static final int IMG_MAX_WIDTH  = 1024;
    private static final int IMG_MAX_HEIGHT = 1024;

    public String getFullPath(String fileName)
    {
        return Paths.get( fileUploadProperties.getDir(), fileName).toString();
    }

    public List<UploadFile> storeFiles(@NotNull List<MultipartFile> multipartFile)
    {
        List<UploadFile> uploadFiles = new ArrayList<>();
        for (MultipartFile file : multipartFile)
        {
            UploadFile uploadFile = storeFile(file);
            if ( uploadFile != null)
                uploadFiles.add(uploadFile);
        }
        return uploadFiles;
    }

    public UploadFile storeFile(@NotNull MultipartFile multipartFile) {
        if ( multipartFile.isEmpty() )
            return null;

        UploadFile uploadFile = new UploadFile();

        String orgFilename  = multipartFile.getOriginalFilename();
        String extension    = FilenameUtils.getExtension(orgFilename);
        String contentType  = multipartFile.getContentType();
        long fileSize       = multipartFile.getSize();
        String saveName     = UUID.randomUUID().toString();
        String saveFilename = String.format("%s.%s", saveName, extension);
        String saveFullPath = getFullPath(saveFilename);

        try{
            File file = new File(saveFullPath);
            multipartFile.transferTo( file );
            String mimeType = Files.probeContentType( file.toPath() );

            uploadFile.setUploadFileName(orgFilename);
            uploadFile.setStoreFileName(saveFilename);
            uploadFile.setFileSize(fileSize);
            uploadFile.setMimeType(mimeType);

            if ( isImage(contentType) ){
                BufferedImage bufferedImage = getBufferedImage(multipartFile);
                if ( bufferedImage != null ){
                    setImageSize(bufferedImage, uploadFile);

                    // 이미지 썸네일
                    BufferedImage resizeImage = resizeImage(bufferedImage, 200, 200);
                    String thumFilename = String.format("%s_200_200.%s", saveName, extension);
                    UploadFile resizeUploadFile = saveResizeImage(resizeImage, 200, 200, thumFilename);
                }
            }

        }catch(IOException e)
        {
            uploadFile.setError(e);
            log.error("storeFile error", e);
        }

        log.info("orgFilename={}, saveFilename={}", orgFilename, saveFilename);
        return uploadFile;
    }


    boolean isImage(final String contentType)
    {
        if (StringUtils.hasText(contentType) )
        {
            if (    contentType.contains("image/jpeg")||
                    contentType.contains("image/jpg") ||
                    contentType.contains("image/png") ||
                    contentType.contains("image/git") )
                return true;
        }
        return false;
    }

    public BufferedImage getBufferedImage(final MultipartFile multipartFile)
    {
        try{
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            return bufferedImage;
        }catch (IOException err) {
            log.info("getBufferedImage error", err);
        }
        return null;
    }

    void setImageSize(final BufferedImage bufferedImage, final UploadFile uploadFile)
    {
        final int width  = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        uploadFile.setImgWidth(width);
        uploadFile.setImgHeight(height);
    }

    boolean saveImageFile(final BufferedImage bufferedImage, String fullpath)
    {
        FileOutputStream fileOutputStream = null;
        String extension = FilenameUtils.getExtension(fullpath);

        try{
            fileOutputStream = new FileOutputStream(fullpath);

            int type = bufferedImage.getType();

            ImageIO.write(bufferedImage, extension, fileOutputStream);
            fileOutputStream.flush();

            fileOutputStream.close();
            return true;

        }catch (IOException err)
        {

        }
        finally {

        }
        return false;
    }

    public UploadFile saveResizeImage(final BufferedImage orgImage, int width, int height, String fullPath)
    {
        BufferedImage resizeImage = resizeImage(orgImage, width, height);
        if ( resizeImage == null ){
            return null;
        }

        int resizeWidth  = resizeImage.getWidth();
        int resizeHeight = resizeImage.getHeight();

        String saveFilename = FilenameUtils.getName(fullPath);
        UploadFile uploadFile = new UploadFile();

        uploadFile.setStoreFileName(saveFilename);
        uploadFile.setImgWidth(resizeWidth);
        uploadFile.setImgHeight(resizeHeight);

        return uploadFile;
    }

    public BufferedImage resizeImage(final BufferedImage orgImage, int width, int height)
    {
        final int ow = orgImage.getWidth();
        final int oh = orgImage.getHeight();

        width  = Math.min(width, IMG_MAX_WIDTH);
        height = Math.min(height, IMG_MAX_HEIGHT);

        // TODO 원본의 가로,세로 비율 유지

        BufferedImage resizeImage = new BufferedImage(width, height, orgImage.getType() );

        AffineTransform transform = new AffineTransform();
        transform.scale(width, height);;

        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
        return transformOp.filter(orgImage, resizeImage);
    }

}
