package com.inho.mvc2.web.common.exceptionresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HandlerExceptionResolver는 WebMvcConfigurer 에서 등록 후 사용가능
 *
 *
 * HandlerExceptionResolver 가 ModelAndView를 반환하는 이유는 마치 try, catch 하듯,
 * Exception을 처리해서 정상 흐름 처럼 변경하는 것이 목적이다.
 * 이금 그대로 Exception을 Resolver (해결)하는 것이 목적이다.
 *
 * 반환 값에 따른 동작 방식
 * HandlerExceptionResolver의 반환 값에 따른 DispatcherServlet의 동작 방식
 * 1) 빈 ModelAndView : 뷰 렌더링 하지 않고 정상 흐름으로 서블릿이 리턴
 * 2) ModelAndView    : 반환하면 뷰를 렌더링
 * 3) null              : 다음 ExceptionResolver를 찾아서 실행,
 *                      만약, 처리할 ExceptionResolver가 없으면 예외 처리가 안되고,
 *                      기존에 발생한 예외를 서블릿 밖으로 던진다.
 *                      
 * HandlerExceptionResolver 활용
 * 예외 상태 코드 변환
 * 뷰 템플릿 처리
 * API 응답 처리 : response.getWriter().println("{state:"300",...}")}
 */
@Slf4j
@Component
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex)
    {
        if ( ex instanceof IllegalArgumentException)
        {
            log.info("IllegalArgumentException resolver to 400");
            try{
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage() );

                // 예외를 먹어버리고 ModelAndView
                return new ModelAndView();

            }catch(IOException e){
                log.error("resolveException ex", e);
            }

        }


        return null;
    }
}
