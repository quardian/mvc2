package com.inho.mvc2.web.common.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

@Configuration
public class MessageSourceConfiguration {

    /**
     * basename 은 기본경로를 resources 경로 기준으로 설정한다.
     *          별도 설정이 없어도, springboot는 /resources/messages.properties를 기본 메시지로 자동 인식한다.
     *
     * application.properties 파일에 메시지 관련설정 하는 법
     * spring.messages.basename=messages/messages,messages/errors
     * spring.messages.encoding=utf-8
     * @return
     *
     * 국제화 언어 선택은 accpet-language : 헤더를 참조하여 결정하거나, 사용자가 설정하게 하여 cookie값으로 관리하여 보여준다.
     */
    @Bean
    public MessageSource messageSource()
    {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

            messageSource.setBasenames("messages/messages", "messages/errors");
            messageSource.setDefaultEncoding("utf-8");

        return messageSource;
    }


    @Bean
    public LocaleResolver localeResolver()
    {
        //AcceptHeaderLocaleResolver();
        //new SessionLocaleResolver();
        CookieLocaleResolver localResolver = new CookieLocaleResolver();

        localResolver.setCookieName("lang");
        localResolver.setDefaultLocale( new Locale("ko") );
        //localResolver.setCookieHttpOnly(true);

        return localResolver;
    }
}
