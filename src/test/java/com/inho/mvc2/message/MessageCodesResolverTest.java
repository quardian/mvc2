package com.inho.mvc2.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){

        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

        /*
            DefaultMessageCodesResolver 기본 메시지 생성규칙 (객체 오류)
            1) code + "." + object name
            2) code
        * */
        Assertions.assertThat(messageCodes).containsExactly("required.item",
                                                            "required");
    }



    @Test
    void messageCodesResolverField(){

        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

           /*
            DefaultMessageCodesResolver 기본 메시지 생성규칙 (필드 오류)
            1) code + "." + object name + "." + field
            2) code + "." + field
            3) code + "." + field type
            4) code
        * */
        //new FieldError("item", "itemName", null, false, new String[]{"required.item.itemName", "required.itemName", "required.java.lang.String", "required"}, null, null);
        Assertions.assertThat(messageCodes).containsExactly("required.item.itemName",
                                                            "required.itemName",
                                                            "required.java.lang.String",
                                                            "required");
    }
}
