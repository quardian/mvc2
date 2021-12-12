package com.inho.mvc2.web.common.formatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class NumberFormatter implements Formatter<Number>{

    /**
     * 1,000 ==> 1000
     * @param text
     * @param locale
     * @return
     * @throws ParseException
     */
    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("NumberFormatter.parse(text={}, locale={})", text, locale);
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }

    /**
     * 1000 ==> 1,000
     * @param object
     * @param locale
     * @return
     */
    @Override
    public String print(Number object, Locale locale)
    {
        log.info("NumberFormatter.print(object={}, locale={})", object, locale);
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        return numberFormat.format(object);
    }
}
