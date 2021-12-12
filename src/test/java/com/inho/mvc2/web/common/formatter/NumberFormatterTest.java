package com.inho.mvc2.web.common.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class NumberFormatterTest {

    NumberFormatter fmt = new NumberFormatter();

    @Test
    void parse() throws ParseException {
        Number result = fmt.parse("1,000", Locale.KOREA);
        Assertions.assertThat(result).isEqualTo(1000L);

        result = fmt.parse("1000.1", Locale.KOREA);
        Assertions.assertThat(result).isEqualTo( BigDecimal.valueOf(1000.1).doubleValue() );
    }

    @Test
    void print() throws ParseException {
        String result = fmt.print(1000L, Locale.KOREA);
        Assertions.assertThat(result).isEqualTo("1,000");

        result = fmt.print(BigDecimal.valueOf(1000.1), Locale.KOREA);
        Assertions.assertThat(result).isEqualTo("1,000.1");
    }
}