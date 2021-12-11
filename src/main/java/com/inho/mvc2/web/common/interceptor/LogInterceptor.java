package com.inho.mvc2.web.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor
{

    public static final String REQUEST_LOG_ID = "logId";

    /**
     * Dispatcher Servelet 에서 컨트롤 호출 하기전
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(REQUEST_LOG_ID, uuid);

        if ( handler instanceof HandlerMethod )
        {//@ReuqestMappaing 을 사용한 경우, HandlerMethod
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Class<? extends HandlerMethod> controllerClass = handlerMethod.getClass();

            log.info("## preHandle [{}][{}][{}]", uuid, requestURI, handler);
        }
        else if ( handler instanceof ResourceHttpRequestHandler)
        {// 정적 리소스 : ResourceHttpRequestHandler

        }


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * Dispatcher Servlet 에서 컨트롤을 정상적으로 호출 후, view Render 하기 전에 호출 
     * Controller에서 Exception 발생시는 호출 안됨
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("## postHandler [{}]", modelAndView);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * Dispatcher Servlet view Render 호출 후 호출된
     * Controller에서 Exception 발생해도 무조건 호출 됨.
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI   = request.getRequestURI();
        String logId        = (String)request.getAttribute(REQUEST_LOG_ID);
        log.info("## afterCompletion [{}][{}][{}]", logId, requestURI, handler);
        if ( ex != null )
        {// 에러가 발생한 경우
            log.error("afterCompletion error", ex);
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
