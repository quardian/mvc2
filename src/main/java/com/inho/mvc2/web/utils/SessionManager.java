package com.inho.mvc2.web.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "sessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * * sessionId 생성
     * * 세션 저장소에 sessionId와 보관할 값 저장
     * * sessionId로 응답 쿠키 생성
     * @param value
     * @param response
     */
    public void createSession(Object value, HttpServletResponse response)
    {
        // [01] sessionId 생성
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(sessionCookie);
    }


    /**
     * 세션 조회
     * @param request
     * @return
     */
    public Object getSession(HttpServletRequest request)
    {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if ( sessionCookie == null ) return null;

        String sessionId = sessionCookie.getValue();
        return sessionStore.get(sessionId);
    }


    /**
     * 세션 만료 처리
     * @param request
     */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if ( sessionCookie == null ) return ;

        String sessionId = sessionCookie.getValue();
        sessionStore.remove(sessionId);
    }


    /**
     * 쿠키 조회
     * @param request
     * @param cookieName
     * @return
     */
    public Cookie findCookie(HttpServletRequest request, String cookieName)
    {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies).filter( c -> c.getName().equals(cookieName) )
                .findFirst()
                .orElse(null);
    }

}
