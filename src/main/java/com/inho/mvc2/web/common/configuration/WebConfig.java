package com.inho.mvc2.web.common.configuration;

import com.inho.mvc2.web.common.argumentresolver.LoginMemberArgumentResolver;
import com.inho.mvc2.web.common.exceptionresolver.MyHandlerExceptionResolver;
import com.inho.mvc2.web.common.exceptionresolver.UserHandlerExceptionResolver;
import com.inho.mvc2.web.common.filter.LogFilter;
import com.inho.mvc2.web.common.filter.LoginCheckFilter;
import com.inho.mvc2.web.common.interceptor.LogInterceptor;
import com.inho.mvc2.web.common.interceptor.LoginCheckInterceptor;
import com.inho.mvc2.web.common.typeconverter.BooleanToOxConverter;
import com.inho.mvc2.web.common.typeconverter.IpPortToStringConverter;
import com.inho.mvc2.web.common.typeconverter.OxToBooleanConverter;
import com.inho.mvc2.web.common.typeconverter.StringToIpPortConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MyHandlerExceptionResolver  myHandlerExceptionResolver;
    private final UserHandlerExceptionResolver userHandlerExceptionResolver;

    /**-----------------------------------------------------------------------------------------
     *
     * WebMvcConfigurer 오버라이드 코드
     *
     -----------------------------------------------------------------------------------------*/
    /**
     * 인터셉터 추가
     * @param registry
     *
     *
     * PathPattern 지정방식
        ?   한 문자 일치
        *   경로(/) 안에서 0개 이상의 문자 일치
        **  경로 끝까지 0개 이상의 경로(/) 일치
        {spring}        경로(/)와 일치하고 spring이라는 변수로 캡처
        {spring:[a-z]+} spring 이름의 경로패스로 a-z 문자가 1 개 이상 일치
        {*spring}       경로가 끝날 때 까지 0개 이상의 경로(/)와 일치하고 spring이라는 변수로 캐처
    {spring:[}

     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor( new LogInterceptor() )
                .order(1)
                .addPathPatterns("/**") // 서블릿하고 패턴이 다름
                .excludePathPatterns("/css/**", "*.ico", "/error");

        registry.addInterceptor( new LoginCheckInterceptor() )
                .order(2)
                .addPathPatterns("/**") // 서블릿하고 패턴이 다름
                .excludePathPatterns("/css/**", "*.ico", "/error");
    }

    /**
     * ArgementResolver 추가
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        // 로그인 ArgumentResolver
        resolvers.add( new LoginMemberArgumentResolver());

        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    /**
     * HandlerExeptionResolver 등록
     * @param resolvers
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

        resolvers.add ( myHandlerExceptionResolver );
        resolvers.add ( userHandlerExceptionResolver );

        WebMvcConfigurer.super.extendHandlerExceptionResolvers(resolvers);
    }

    /**
     * Convertor를 등록한다.
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {

        // 컨버터 등록 : 스프링 기본 등록된 것 중복되면, 우리가 등록한 걸 우선한다.
        registry.addConverter( new BooleanToOxConverter() );
        registry.addConverter( new OxToBooleanConverter() );
        registry.addConverter( new StringToIpPortConverter() );
        registry.addConverter( new IpPortToStringConverter() );

        WebMvcConfigurer.super.addFormatters(registry);
    }

/*
    HandlerExceptionResolver를 등록하기 위해 이 메소드 사용시
    스프링이 기본으로 등록하는 ExceptionResolver가 제거되므로 주의,
    extendHandlerExceptionResolvers를 사용하자.

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        WebMvcConfigurer.super.configureHandlerExceptionResolvers(resolvers);
    }*/
/**-----------------------------------------------------------------------------------------
     *
     * 필터 등록 ( Bean으로 등록하면 WAS가 Singleton으로 관리한다. )
     * 
     -----------------------------------------------------------------------------------------*/
    /**
     * 내가 작성한 logFilter 등록방법
     * @return
     */
    //@Bean
    public FilterRegistrationBean logFilter()
    {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

                /*

        DispatcherType 종류는 아래와 같다.
        - REQUEST   : 클라이언트 요청
        - ERROR     : 오류 요청
        - FOWARD    : RequestDispatcher.forward(request, response)
        - INCLUDE   : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때 RequestDispatcher.include(request, response)
        - ASYNC     : 서블릿 비동기 호출

        * */

        filterRegistrationBean.setFilter( new LogFilter() );
        filterRegistrationBean.setOrder(1);                 // 순서가 낮을 수록 먼저 동작.
        filterRegistrationBean.addUrlPatterns("/*");        // 필터가 처리할 URL 패턴
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // 기본값은 REQUEST 만 설정된다.
        return filterRegistrationBean;
    }


    /**
     * 로그인 체크 필터
     * @return
     */
    // @Bean
    public FilterRegistrationBean loginCheckFilter()
    {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter( new LoginCheckFilter() );
        filterRegistrationBean.setOrder(2);                 // 순서가 낮을 수록 먼저 동작.
        filterRegistrationBean.addUrlPatterns("/*");        // 필터가 처리할 URL 패턴
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistrationBean;
    }
}
