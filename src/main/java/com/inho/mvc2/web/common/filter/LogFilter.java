package com.inho.mvc2.web.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 필터 등록 방법
 * == web/config/WebConfig
 *      FilterRegistrationBean Bean등록
 *      
 * @WebFilter 애노테이션으로 등록이 가능하나 순서지정이 안되므로, FilterRegistrationBean 으로 등록하길 추천
 *
 * logback MDC 참조..
 */
@Slf4j
//@WebFilter(filterName = "logFilter", urlPatterns = "/*")
public class LogFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        HttpServletRequest  req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        try{
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            //MDC.put("_req_uuid", uuid);
            //req.setAttribute("_req_uuid", uuid);
            chain.doFilter(request, response);
        }catch(Exception e){

        }finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }


    @Override
    public void destroy() {
        log.info("log filter destroy");
        Filter.super.destroy();
    }
}
