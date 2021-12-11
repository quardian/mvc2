package com.inho.mvc2.web.controller.typeconvertor;

import com.inho.mvc2.web.model.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 스프링은 용도에 따라 다양한 방식의 타입 컨버터 제공
 * Converter            : 기본 타입 컨버터
 * ConverterFactory     : 전체 클래스 계층 구조가 필요할 때
 * GenericConverter     : 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
 * ConditionalGenericConverter  : 특정 조건이 참인 경우에만 실행
 */

@Slf4j
@RestController
@RequestMapping("/typeconvertor")
public class HelloController
{

    @GetMapping("hello-v1")
    public String helloV1(HttpServletRequest request)
    {
        String data = request.getParameter("data"); // 문자 타입 조회
        Integer intValue = Integer.valueOf(data);         // 숫자 타입 변환

        log.info("intValue = {}", intValue);

        return "OK";
    }


    @GetMapping()
    public String helloV2(@RequestParam("ox") boolean ox,
                          @RequestParam("ip") IpPort ipPort)
    {
        log.info("ox = {}, ipPort=", ox, ipPort);
        return "OK";
    }


}
