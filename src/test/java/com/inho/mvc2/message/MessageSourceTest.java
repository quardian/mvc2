package com.inho.mvc2.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){

        String hello = ms.getMessage("hello", null, null);
        assertThat(hello).isEqualTo("안녕");

        String helloName = ms.getMessage("hello.name", new Object[]{"인호"}, Locale.KOREA);
        assertThat(helloName).isEqualTo("안녕 인호");

        String helloNameEn = ms.getMessage("hello.name", new Object[]{"inho"}, Locale.ENGLISH);
        assertThat(helloNameEn).isEqualTo("hello inho");
    }

    @Test
    void notFoundMessageCode()
    {
        Assertions.assertThatThrownBy( () -> ms.getMessage("no_code", null, null) )
                .isInstanceOf(NoSuchMessageException.class);

        String message = ms.getMessage("no_code", null, "기본메시지", null);
        assertThat(message).isEqualTo("기본메시지");
    }

}
