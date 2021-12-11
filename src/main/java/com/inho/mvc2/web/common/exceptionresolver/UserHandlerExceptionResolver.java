package com.inho.mvc2.web.common.exceptionresolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inho.mvc2.web.common.exceptions.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/*
    API 예외처리 - 스프링이 제공하는 ExceptionResolver 1
        스프링 부트가 기본으로 제공하는 ExceptionResolver 는 다음과 같다
        1) ExceptionHandlerExceptionResolver
        2) ResponseStatusExceptionResolver
        3) DefaultHandlerExceptionResolver <- 우선 순위가 가장 낮다.

    * ExceptionHandlerExceptionResolver
        @ExceptionHandler 을 처리한다. API 예외 처리는 대부분 이 기능으로 해결된다.

    * ResponseStatusExceptionResolver
        HTTP 상태코드를 지정해준다.
        @ResponseStatus(value= HttpStatus.NOT_FOUOND)
        ReponseStatusException 예외 발생

    * DefaultHandlerExceptionResolver
        스프링 내부 기본 예외를 처리한다.

 */

@Slf4j
@RequiredArgsConstructor
@Component
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper;

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {

        try{
            if ( ex instanceof UserException)
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                String acceptHeader = request.getHeader("accept");
                boolean isJson = MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader);
                if ( isJson )
                {
                    Map<String, Object> errorResult = new LinkedHashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write( objectMapper.writeValueAsString(errorResult) );

                    return new ModelAndView();
                }
                else {
                    return new ModelAndView("error/500");
                }
            }
        }
        catch (IOException e)
        {
            log.error("resolveException :", e);
        }

        return null;
    }
}
