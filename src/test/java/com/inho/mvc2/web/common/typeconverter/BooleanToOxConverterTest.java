package com.inho.mvc2.web.common.typeconverter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanToOxConverterTest {
    @Test
    void boolToOx()
    {
        BooleanToOxConverter converter = new BooleanToOxConverter();
        Boolean source = true;
        String target = null;

        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo("O");

        source = false;
        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo("X");

        source = null;
        target = converter.convert(source);
        Assertions.assertThat(target).isNull();

    }
}