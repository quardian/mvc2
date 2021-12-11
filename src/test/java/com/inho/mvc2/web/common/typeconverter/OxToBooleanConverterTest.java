package com.inho.mvc2.web.common.typeconverter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OxToBooleanConverterTest {
    @Test
    void OxToBool()
    {
       OxToBooleanConverter  converter = new OxToBooleanConverter();

        String source = "O";
        Boolean target = null;
        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo(true);

        source = "o";
        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo(true);


        source = "X";
        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo(false);

        source = "x";
        target = converter.convert(source);
        Assertions.assertThat(target).isEqualTo(false);

        source = "D";
        target = converter.convert(source);
        Assertions.assertThat(target).isNull();

        source = "null";
        target = converter.convert(source);
        Assertions.assertThat(target).isNull();
    }
}