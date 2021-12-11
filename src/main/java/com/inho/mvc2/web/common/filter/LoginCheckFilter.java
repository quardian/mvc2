package com.inho.mvc2.web.common.filter;

import com.inho.mvc2.web.model.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * was -> filter chain -> servlet(Dispatcher servlet) -> intercepter chain -> controller
 *
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
public class LoginCheckFilter implements Filter {
    
    // URL 방식 보다는 애노테이션 방식이 도 좋을 듯 : 그럴려면 인터셉터에서 처리해야 함.
    private static final String[] whitelist = {"/", "members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("loginCheckFilter doFilter");

        HttpServletRequest  req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        try{
            log.info("인증 체크 필터 시작 {}", requestURI);
            if ( !isWhiteListURL(requestURI) ){
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = req.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null ){
                    log.info("미인증 사용자 요청 {}", requestURI);
                    res.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            chain.doFilter(request, response);
        }
        catch(Exception e)
        {
            throw e; // 예외 로깅 가능 하지만, 톰캣까지 예외를 보내줘야 함.
        }finally {
            log.info("인증 체크 필터 종료 {}",  requestURI);
        }
    }

    /**
     * 화이트 리스트 여부
     * @param requestURI
     * @return
     */
    private boolean isWhiteListURL(String requestURI)
    {
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

}
