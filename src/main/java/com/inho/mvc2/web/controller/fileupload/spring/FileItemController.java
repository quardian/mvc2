package com.inho.mvc2.web.controller.fileupload.spring;

import com.inho.mvc2.web.controller.fileupload.spring.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/fileupload/spring/items")
public class FileItemController
{
    private final FileItemRepository fileItemRepository;
    private final FileStoreService fileStoreService;


    @GetMapping("new")
    public String newForm(@ModelAttribute("form") ItemForm form)
    {
        return "fileupload/spring/newForm";
    }


    @PostMapping("new")
    public String newFormPost(@ModelAttribute("form") ItemForm form,
                              RedirectAttributes redirectAttributes)
    {
        // 파일 업로드 처리
        MultipartFile attachFile = form.getAttachFile();
        UploadFile uploadFile = fileStoreService.storeFile(attachFile);
        
        List<MultipartFile> imageFiles = form.getImageFiles();
        List<UploadFile> uploadFiles = fileStoreService.storeFiles(imageFiles);

        // DB 저장
        FileItem item = new FileItem();
        item.setItemName( form.getItemName());
        item.setAttachFile(uploadFile);
        item.setImageFiles(uploadFiles);
        fileItemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId() );
        return "redirect:/fileupload/spring/items/{itemId}";
    }


    @GetMapping("{id}")
    public String items(@PathVariable Long id,
                        Model model)

    {
        FileItem item = fileItemRepository.findById(id);
        model.addAttribute("item", item);
        return "fileupload/spring/view";
    }

}
