package com.inho.mvc2.web.controller.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * https://getbootstrap.kr/docs/5.1/getting-started/introduction/
 */
@Controller
@Slf4j
@RequestMapping("/bootstrap")
public class BootStrapController {

    @GetMapping("/form-control")
    public String formControl()
    {
        return "bootstrap/form-control";
    }

    @GetMapping("/select")
    public String select()
    {
        return "bootstrap/select";
    }


    @GetMapping("/checkbox-radio")
    public String checkboxRadio()
    {
        return "bootstrap/checkbox-radio";
    }


    @GetMapping("/range")
    public String range()
    {
        return "bootstrap/range";
    }


    @GetMapping("/input-group")
    public String inputGroup()
    {
        return "bootstrap/input-group";
    }


    @GetMapping("/floating-label")
    public String floatingLabel()
    {
        return "bootstrap/floating-label";
    }
}
