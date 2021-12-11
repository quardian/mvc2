package com.inho.mvc2.web.common.typeconverter;

import com.inho.mvc2.web.model.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IpPortToStringConverterTest {
    @Test
    void ipPortToString()
    {
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);
        String target = null;

        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo("127.0.0.1:8080");

    }
}