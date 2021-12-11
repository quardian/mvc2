package com.inho.mvc2.web.common.typeconverter;

import com.inho.mvc2.web.model.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.util.UriBuilder;

import java.net.URL;

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
public class StringToIpPortConverter implements Converter<String, IpPort>
{
    @Override
    public IpPort convert(String source) {
        IpPort target = null;

        String[] split = source.split(":");
        String ip   = split[0];
        String port = split[1];

        target = new IpPort( ip, Integer.valueOf(port) );
        log.info("StringToIpPortConverter.convert S={}, T={}", source, target);

        return target;
    }

}
