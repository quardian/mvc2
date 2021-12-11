package com.inho.mvc2.web.controller.exeptions.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/*
    예외 발생과 오류 페이지 요청 흐름

    1. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터넷터 <- 컨트롤러(예외발생)
    2. WAS '/error-page/500' 다시 요청 --> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500) -> View

    WAS는 오류발생시, 오류 페이지 호출을 위해 다시 WAS 내부에서 다시 한번 호출하는데,
    이때 필터, 서블릿, 인터셉터도 모두 다시 호출된다. (비효율적)
    결국, 클라이언트로 부터 발생한 정상 요청과, 오류 페이지 출력을 하기 위한 내부 요청인지 구분할 수 있어야 하는데.
    서블릿은 이런 문제를 해결하기 위해 DispatcherType 이라는 추가 정보를 제공한다.

    DispatcherType
        필터는 dispatcherTypes 라는 옵션을 제공한다.
        오류 페이지에서 dispatcherType은 ERROR로 나오며,
        고객이 요청한 경우에는 REQUEST로 나온다.

        DispatcherType 종류는 아래와 같다.
        - REQUEST   : 클라이언트 요청
        - ERROR     : 오류 요청
        - FOWARD    : RequestDispatcher.forward(request, response)
        - INCLUDE   : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때 RequestDispatcher.include(request, response)
        - ASYNC     : 서블릿 비동기 호출
*/


@Slf4j
@Controller
@RequestMapping("/error-page")
public class ErrorPageController {

    @RequestMapping("/404")
    public String errorPage404(Model model,
                               HttpServletRequest request,
                               HttpServletResponse response)
    {
        printErrorInfo(request);
        return "/error-page/404";
    }


    @RequestMapping("/500")
    public String errorPage500(Model model,
                               HttpServletRequest request,
                               HttpServletResponse response)
    {
        printErrorInfo(request);
        return "/error-page/500";
    }

    @RequestMapping(value = "/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> errorPage500Json(Model model,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response)
    {
        printErrorInfo(request);

        Map<String,Object> result = new HashMap<>();

        Exception ex        = (Exception) request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE);
        Integer status       = (Integer)request.getAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE);
        //Integer statusCode  = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        result.put("status", status);
        result.put("message", ex.getMessage());

        return new ResponseEntity<>(result, HttpStatus.valueOf(status) );
    }


    private void printErrorInfo(HttpServletRequest request)
    {
        log.info("ERROR_EXCEPTION: {}", request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) );
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(WebUtils.ERROR_EXCEPTION_TYPE_ATTRIBUTE) );
        log.info("ERROR_MESSAGE: {}", request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE) );
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(WebUtils.ERROR_REQUEST_URI_ATTRIBUTE) );
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(WebUtils.ERROR_SERVLET_NAME_ATTRIBUTE) );
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE) );
        log.info("dispatcherType: {}", request.getDispatcherType() );
    }



}
