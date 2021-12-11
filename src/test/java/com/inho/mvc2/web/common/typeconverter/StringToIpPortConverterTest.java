package com.inho.mvc2.web.common.typeconverter;

import com.inho.mvc2.web.model.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringToIpPortConverterTest {
    @Test
    void stringToIpPort()
    {
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String  source = "127.0.0.1:8080";
        IpPort target  = null;

        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo(new IpPort("127.0.0.1",8080));


    }
}