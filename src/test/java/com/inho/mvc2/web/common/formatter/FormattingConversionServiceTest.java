package com.inho.mvc2.web.common.formatter;

import com.inho.mvc2.web.common.typeconverter.BooleanToOxConverter;
import com.inho.mvc2.web.common.typeconverter.IpPortToStringConverter;
import com.inho.mvc2.web.common.typeconverter.OxToBooleanConverter;
import com.inho.mvc2.web.common.typeconverter.StringToIpPortConverter;
import com.inho.mvc2.web.model.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

class FormattingConversionServiceTest {

    /**
     * FormattingConversionService는 포매터를 지원하는 컨버전 서비스 이다.
     * DefaultFormattingConversionService는 FormattingConversionService에 기본적인 통화, 숫자 관련 몇가지 기본 포맷터를 추가해서 제공한다.
     */
    DefaultFormattingConversionService cs = new DefaultFormattingConversionService();

    @Test
    void formattingConversionService() throws ParseException {

        // 컨버터 등록
        cs.addConverter( new StringToIpPortConverter() );
        cs.addConverter( new IpPortToStringConverter() );
        cs.addConverter( new OxToBooleanConverter() );
        cs.addConverter( new BooleanToOxConverter() );

        // 포맷터 등록
        cs.addFormatter( new NumberFormatter() );

        // 컨버터 사용
        Boolean  r_o     = cs.convert("O", Boolean.class);
        Assertions.assertThat(r_o).isEqualTo(true);

        String r_true  = cs.convert(true, String.class);
        Assertions.assertThat(r_true).isEqualTo("O");

        IpPort r_ipPort = cs.convert("192.168.0.9:2727", IpPort.class);
        Assertions.assertThat(r_ipPort).isEqualTo( new IpPort("192.168.0.9",2727));

        String r_ipStr  = cs.convert(new IpPort("8.8.8.8", 51), String.class);
        Assertions.assertThat(r_ipStr).isEqualTo( "8.8.8.8:51");


        // 포매터 사용
        String convert = cs.convert(1000.1, String.class);
        Assertions.assertThat(convert).isEqualTo("1,000.1");

        Number convert1 = cs.convert("1,000.1", Number.class);
        Assertions.assertThat(convert1).isEqualTo(1000.1);

    }

}