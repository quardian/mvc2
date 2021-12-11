package com.inho.mvc2.web.common.typeconverter;

import com.inho.mvc2.web.model.IpPort;
import groovy.util.logging.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

@Slf4j
public class ConversionServiceTest {
    @Test
    void conversionService()
    {
        // 컨버터 등록 서비스
        DefaultConversionService cs = new DefaultConversionService();

        // 컨버터 등록
        cs.addConverter(new StringToIpPortConverter());
        cs.addConverter(new IpPortToStringConverter());
        cs.addConverter(new OxToBooleanConverter());
        cs.addConverter(new BooleanToOxConverter());

        // 사용
        Boolean  r_o     = cs.convert("O", Boolean.class);
        Assertions.assertThat(r_o).isEqualTo(true);

        String r_true  = cs.convert(true, String.class);
        Assertions.assertThat(r_true).isEqualTo("O");

        IpPort r_ipPort = cs.convert("192.168.0.9:2727", IpPort.class);
        Assertions.assertThat(r_ipPort).isEqualTo( new IpPort("192.168.0.9",2727));

        String r_ipStr  = cs.convert(new IpPort("8.8.8.8", 51), String.class);
        Assertions.assertThat(r_ipStr).isEqualTo( "8.8.8.8:51");
    }
}
