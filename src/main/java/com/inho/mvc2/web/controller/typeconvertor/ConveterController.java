package com.inho.mvc2.web.controller.typeconvertor;

import com.inho.mvc2.web.model.IpPort;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 스프링은 용도에 따라 다양한 방식의 타입 컨버터 제공
 * Converter            : 기본 타입 컨버터
 * ConverterFactory     : 전체 클래스 계층 구조가 필요할 때
 * GenericConverter     : 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
 * ConditionalGenericConverter  : 특정 조건이 참인 경우에만 실행
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/typeconvertor")
public class ConveterController
{
    private  final ConversionService cs;
    @GetMapping("/converterView")
    public String converterView(
                            Model model,
                            @RequestParam("ox") boolean ox,
                          @RequestParam("ip") IpPort ipPort)
    {
        log.info("ox = {}, ipPort=", ox, ipPort);

        model.addAttribute("ox", ox);
        model.addAttribute("ipPort", ipPort);

        return "typeconvertor/converterView";
    }

    @GetMapping("/converterEdit")
    public String converterForm(
            Model model,
            @ModelAttribute("form") Form form)
    {
        IpPort ipPort = new IpPort("8.8.8.8", 51);
        form.setIpPort(ipPort);
        form.setOx(false);
        return "typeconvertor/converterEdit";
    }

    @PostMapping("/converterEdit")
    public String converterFormPost(
            Model model,
            @ModelAttribute("form") Form form)
    {
        // 수동으로 컨버팅
        String ip = cs.convert(form.getIpPort(), String.class);
        String ox = cs.convert(form.isOx(), String.class);

        model.addAttribute("ipPort", form.getIpPort());
        model.addAttribute("ox", ox);
        return "typeconvertor/converterView";
    }




    @Data
    static class Form
    {
        private boolean ox;
        private IpPort  ipPort;

        public Form(boolean ox, IpPort ipPort) {
            this.ox = ox;
            this.ipPort = ipPort;
        }

        public Form() {
        }
    }
}
