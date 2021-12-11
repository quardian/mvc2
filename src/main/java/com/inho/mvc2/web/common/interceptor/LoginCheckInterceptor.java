package com.inho.mvc2.web.common.interceptor;

import com.inho.mvc2.web.common.annotation.LoginRequired;
import com.inho.mvc2.web.model.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.UUID;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor
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

        /**
         *         DispatcherType 종류는 아래와 같다.
         *         - REQUEST   : 클라이언트 요청
         *         - ERROR     : 오류 요청 (WAS가 호출)
         *         - FOWARD    : RequestDispatcher.forward(request, response)
         *         - INCLUDE   : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때 RequestDispatcher.include(request, response)
         *         - ASYNC     : 서블릿 비동기 호출
         */
        DispatcherType dispatcherType = request.getDispatcherType();

        if ( dispatcherType == DispatcherType.REQUEST && handler instanceof HandlerMethod ){
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Class<?> controller = handlerMethod.getBeanType();
            Method method       = handlerMethod.getMethod();

            LoginRequired controllerReuqired    = controller.getAnnotation(LoginRequired.class);
            LoginRequired methodReuqired        = method.getAnnotation(LoginRequired.class);
            LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
            if ( (methodReuqired == null && controllerReuqired != null && controllerReuqired.value()) ||
                    methodReuqired != null && methodReuqired.value() ) 
            {// 로그인 체크 : 메소드에 설정이 없지만, Controller에 설정된 경우 또는 Method에 설정된 경우
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = request.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null ){
                    log.info("미인증 사용자 요청 {}", requestURI);
                    response.sendRedirect("/login?redirectURL=" + requestURI);
                    return false;
                }
            }
            else
            {
                log.info("로그인 체크 SKIP");
            }

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
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
