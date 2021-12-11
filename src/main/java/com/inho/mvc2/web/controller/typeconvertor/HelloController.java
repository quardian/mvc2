package com.inho.mvc2.web.controller.typeconvertor;

import com.inho.mvc2.web.model.IpPort;
import lombok.extern.slf4j.Slf4j;
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
@Controller
@RequestMapping("/typeconvertor")
public class HelloController
{

    @GetMapping("hello-v1")
    @ResponseBody
    public String helloV1(HttpServletRequest request)
    {
        String data = request.getParameter("data"); // 문자 타입 조회
        Integer intValue = Integer.valueOf(data);         // 숫자 타입 변환

        log.info("intValue = {}", intValue);

        return "OK";
    }


    @GetMapping()
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


}
