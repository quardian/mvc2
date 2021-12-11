package com.inho.mvc2.web.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인 체크 여부 판단하는 애노테이션
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired{
    /**
     * 로그인 체크 여부 : 기본값 true
     * @return
     */
    boolean value() default true;
}
