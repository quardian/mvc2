package com.inho.mvc2.domain.model;

import org.springframework.stereotype.Component;

@Component("helloBean")
public class HelloBean {
    public String hello(String data)
    {
        return "Hello " + data;
    }
}
