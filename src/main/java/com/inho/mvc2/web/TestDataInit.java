package com.inho.mvc2.web;

import com.inho.mvc2.domain.model.Item;
import com.inho.mvc2.domain.model.member.Member;
import com.inho.mvc2.domain.repository.item.ItemRepository;
import com.inho.mvc2.domain.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;


    @PostConstruct
    public void init()
    {
        itemRepository.save( new Item("상품A", 1000, 10));
        itemRepository.save( new Item("상품B", 5000, 20));

        memberRepository.save( new Member("test", "이인호", "1111") ) ;
    }

    @PreDestroy
    public void destroy()
    {

    }


}
