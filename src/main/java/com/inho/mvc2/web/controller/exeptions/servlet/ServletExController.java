package com.inho.mvc2.web.controller.exeptions.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/exceptions")
public class ServletExController {

    /**
     *
     * #스프링 부트가 제공하는 기본 예외 페이지 끄기 (예외처리 확인용)
     * application.properties
     * server.error.whitelabel.enabled=false
     *
     * WAS가 에러 처리 시,
     * 결과) HTTP Status 500 – Internal Server Error
     */
    @GetMapping("/error-ex")
    public void errorEx(){
        throw new RuntimeException("예외 발생!");
    }


    /**
     * sendError로 404, 500 등 오류코드를 WAS에 전달할 수 있다.
     * sendError 흐름
     * WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(response.sendError)
     * 컨트롤러가 sendError 설정하면, exception 없이 WAS까지 전달이 되며, WAS가 최종 응답 전
     * sendError가 설정되었다면, WAS의 기본 오류 페이지 내용을 보여준다.
     * @param response
     * @throws IOException
     */
    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500, "500 오류");
    }
}
