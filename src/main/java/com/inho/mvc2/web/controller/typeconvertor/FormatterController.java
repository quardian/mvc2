package com.inho.mvc2.web.controller.typeconvertor;

import com.inho.mvc2.web.model.IpPort;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**

 
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/formatter")
public class FormatterController
{
    private  final ConversionService cs;
    @GetMapping("/edit")
    public String form( Model model,
                        @ModelAttribute("form") Form form )
    {
        form.setNumber( 10000 );
        form.setLocalDateTime( LocalDateTime.now() );

        return "formatter/edit";
    }


    @PostMapping("/edit")
    public String formPost( Model model,
                        @ModelAttribute("form") Form form )
    {
        log.info("form={}", form);
        return "formatter/edit";
    }

    @Data
    static class Form
    {
        @NumberFormat(pattern = "###,###")
        private Integer number;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;

        public Form() {
        }
    }
}
