package com.inho.mvc2.web.controller.exeptions.api;

import com.inho.mvc2.domain.model.User;
import com.inho.mvc2.domain.model.member.Member;
import com.inho.mvc2.domain.repository.member.MemberRepository;
import com.inho.mvc2.web.common.exceptions.BadRequestException;
import com.inho.mvc2.web.common.exceptions.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiExceptionController
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


    /**
     * 스프링의 ResponseStatusExceptionResolver 를 이용한 에러 처리
     * @return
     */
    @GetMapping("/response-status-ex1")
    public Member responseStatusEx1() {
        throw new BadRequestException("responseStatusEx1");
    }

    /**
     * 스프링의 ResponseStatusExceptionResolver 를 이용한 에러 처리
     * @return
     */
    @GetMapping("/response-status-ex2")
    public Member responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "error.not.found",
                new IllegalArgumentException());
    }

    /**
     * 스프링의 DefaultHandlerExceptionResolver
     *
     * 스프링 내부에서 발생하는 스프링 예외를 해결한다.
     * 대표적으로 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 TypeMismatchException 이 발생하는데,
     * 이 경우 예외가 발생했기 때문에 그냥 두면 WAS에서 500 오류가 발생한다.
     * 그런데 파라미터 바인딩은 대부분 클라이언트가 요청을 잘못해서 발생한다.
     * HTTP에서는 이런 경우 HTTP상태 400을 사용하도록 되어있다.
     *
     * DefaultHandlerExceptionResolver는 이것을 500 오류가 아닌 400오류로 변경한다.
     * @param data
     * @return
     */
    @GetMapping("/default-handler-ex")
    public String defaultHandlerException(@RequestParam Integer data) {
        return "OK";
    }


    /*
        @ExceptionHandler
        스프링은 API 예외 처리 문제를 해결하기 위해 @ExceptionHandler 라는 애노테이션을 사용하는 예외처리 기능을 제공하는데,
        이것은 ExceptionHandlerExceptionResolver 이다. (스프링 기본 제공 )
        ExceptionResolver 중에 우선 순위가 가장 높다. API 예외 처리는 대부분 이 기능을 사용한다.
     */



}
