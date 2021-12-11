package com.inho.mvc2.web.controller.exeptions.api;

import com.inho.mvc2.domain.model.member.Member;
import com.inho.mvc2.domain.repository.member.MemberRepository;
import com.inho.mvc2.web.common.exceptions.BadRequestException;
import com.inho.mvc2.web.common.exceptions.UserException;
import com.inho.mvc2.web.model.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ApiExceptionV2Controller
{
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public Member member(@PathVariable("id") String id) {
        Long intId = 0L;
        try{
            intId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("id 형식이 올바르지 않습니다.");
        }

        if ( intId > 100){
            throw new UserException("100을 넘을 수 없습니다.");
        }

        Member member = memberRepository.findById(intId);
        if ( member == null ){
            throw new RuntimeException("알수없는 사용자 입니다.");
        }


        return member;
    }

}
