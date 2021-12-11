package com.inho.mvc2.web.common.configuration;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;

/**
 * springboot의 웹서버를 커스터마이징 한다.
 *  web.xml에서 설정하던 정보 등
     * 1) 서버주소, 포트 지정 가능
     * 2) 에러페이지 지정가능
     * 3) 서버의 기본 커넥터에 적용될 SSL 환경설정
     * 4) HTTP2 환경
     * 5) 서버의 기본 커넥터에 적용될 압축 환경
     * 6) 서버 헤더값
     * 7) 서버에 적용될 shutdown 환경 설정
 *
   springboot는 ErrorPage를 자동으로 등록한다.
    이때 /error 라는 경로로 기본 오류 페이지를 설정한다.
    new ErrorPage("/error"), 상태코드와 예외를 설정하지 않으면 기본 오류 페이지로 사용된다.

    서블릿 밖으로 예외가 발생하거나, rsponse.sendError()가 호출되면 모든 오류는 /error를 호출하게 왼다.
    BasicErrorController 라는 스프링 컨트롤러를 자동으로 등록한다.
        ErrorPage에서 등록한 /error를 매핑해서 처리하는 컨트롤러다

    ErrorMvcAutoConfiguration 이라는 클래스가 오류 페이지를 자동으로 등록하는 역할을 한다.

    개발자는 옲 페이지만 등록
        BasicErrorController는 기본적인 로직이 모두 개발되어 있다.
        개발자는 오류 페이지 화면만 BasicErrorController가 제공하는 룰과 우선순위에 따라 등록하면 된다.
        정적 HTML이면 정적 리소스, 뷰템플릿을 사용해서 동적으로 오류 화면을 만들고 싶으면 뷰 템플릿 경로에 오류 페이지 파일을 만들어서 넣어두기만 하면 된다.


         server.error.whitelabel.enabled=false  : 오류 처리 화면 못찰을 시 스프링 whitelabel 오류 페이지 적용여부
         server.error.path=/error               : 오류 페이지 경로, 스프링이 자동 등록하는 서블릭 글로벌 오류 페이지 경로와 오류 컨트롤러 경로에 함께 사용한다.
         server.error.include-exception=true
         server.error.include-binding-errors=always
         server.error.include-message=always
         server.error.include-stacktrace=always


    * 뷰 선택 우선순위
 *      BasicErrorController 의 처리순서
        1. 뷰 템플릿
            resources/templates/error/500.html
            resources/templates/error/5xx.html
        2. 정적 리소스(static, public)
            resources/static/error/400.html
            resources/static/error/404.html
            resources/static/error/4xx.html
        3. 적용 대상이 없을 때 뷰 이름('error')
            resources/templates/error.html

 */
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        // 개발환경 마다 다른 서버 포트 사용설정 가능
        // properties에서 server.port로 설정했더라도, 소스코드 상의 포트설정이 우선된다.
        factory.setPort(8080);

        // 에러 코드별 에러페이지 지정
        // 오류 발생시 오류내용이 WAS 까지 전달되었다가, 아래 지정된 오류페이지를 호출한다.
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        // 에러 타입별 에러페이지 지정
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);

    }
}
