package com.inho.mvc2.web.common.typeconverter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * true, false을 O, X 문자열로  변환
 *
 * WebMvcConfigurer
 *  @Override
 *     public void addFormatters(FormatterRegistry registry) {
 *
 *         registry.addConverter( new BooleanToOxConverter() );
 *         registry.addConverter( new OxToBooleanConverter() );
 *         registry.addConverter( new StringToIpPortConverter() );
 *         registry.addConverter( new IpPortToStringConverter() );
 *
 *         WebMvcConfigurer.super.addFormatters(registry);
 *     }
 */
@Slf4j
public class BooleanToOxConverter implements Converter<Boolean, String>
{
    @Override
    public String convert(Boolean source) {
        String target = null;
        if ( source != null ){
            target = source ? "O" : "X";
        }
        log.info("BooleanToOxConverter.convert S={}, T={}", source, target);
        return target;
    }

}
