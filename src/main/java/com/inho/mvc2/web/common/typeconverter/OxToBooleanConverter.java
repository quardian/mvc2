package com.inho.mvc2.web.common.typeconverter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * O, X 문자열을 true, false로 변환
 *
 *  * WebMvcConfigurer
 *  *  @Override
 *  *     public void addFormatters(FormatterRegistry registry) {
 *  *
 *  *         registry.addConverter( new BooleanToOxConverter() );
 *  *         registry.addConverter( new OxToBooleanConverter() );
 *  *         registry.addConverter( new StringToIpPortConverter() );
 *  *         registry.addConverter( new IpPortToStringConverter() );
 *  *
 *  *         WebMvcConfigurer.super.addFormatters(registry);
 *  *     }
 */
@Slf4j
public class OxToBooleanConverter implements Converter<String, Boolean>
{
    @Override
    public Boolean convert(String source) {
        Boolean target = null;
        if ("O".equalsIgnoreCase(source) )
            target  = true;
        else if ( "X".equalsIgnoreCase(source))
            target = false;

        log.info("OxToBooleanConverter.convert S={}, T={}", source, target);

        return target;
    }

}
