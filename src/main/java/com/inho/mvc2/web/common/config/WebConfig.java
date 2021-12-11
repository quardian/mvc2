package com.inho.mvc2.web.common.config;

import com.inho.mvc2.web.common.argumentresolver.LoginMemberArgumentResolver;
import com.inho.mvc2.web.common.filter.LogFilter;
import com.inho.mvc2.web.common.filter.LoginCheckFilter;
import com.inho.mvc2.web.common.interceptor.LogInterceptor;
import com.inho.mvc2.web.common.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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

        filterRegistrationBean.setFilter( new LogFilter() );
        filterRegistrationBean.setOrder(1);                 // 순서가 낮을 수록 먼저 동작.
        filterRegistrationBean.addUrlPatterns("/*");        // 필터가 처리할 URL 패턴

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

        return filterRegistrationBean;
    }
}
