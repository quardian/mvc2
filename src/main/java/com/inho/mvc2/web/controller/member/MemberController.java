package com.inho.mvc2.web.controller.member;

import com.inho.mvc2.domain.model.member.Member;
import com.inho.mvc2.domain.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;


    /**
     * 회원가입 폼
     * GET members/add
     * @param model
     * @param member
     * @return
     */
    @GetMapping("/add")
    public String addForm(Model model,
                          @ModelAttribute("member") Member member)
    {
        return "member/add";
    }


    /**
     * 회원가입 처리
     * POST members/add
     * @param model
     * @param member
     * @param bindingResult
     * @return
     */
    @PostMapping("/add")
    public String add(Model model,
                      @Validated @ModelAttribute("member") Member member,
                      BindingResult bindingResult)
    {
        if ( bindingResult.hasErrors() ){
            return "member/add";
        }
        memberRepository.save(member);
        return "redirect:/";
    }
}
