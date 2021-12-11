package com.inho.mvc2.web.common.typeconverter;

import com.inho.mvc2.web.model.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * ip:port 형태의 문자열을 IpPort 객체로 변환
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
public class IpPortToStringConverter implements Converter<IpPort, String>
{
    @Override
    public String convert(IpPort source) {
        String target = null;

        if ( source != null ){
            target = String.format("%s:%d", source.getIp(), source.getPort());
        }
        log.info("IpPortToStringConverter.convert S={}, T={}", source, target);
        return target;
    }

}
