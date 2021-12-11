package com.inho.mvc2.web.controller;

import com.inho.mvc2.domain.model.member.Member;
import com.inho.mvc2.domain.repository.member.MemberRepository;
import com.inho.mvc2.web.common.annotation.Login;
import com.inho.mvc2.web.model.SessionConst;
import com.inho.mvc2.web.utils.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping
    public String home( HttpServletRequest request,
                       Model model)
    {
        //Member member = (Member)sessionManager.getSession(request);
        HttpSession session = request.getSession(false);
        Member member = null;
        if ( session != null ){
            member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        }
        if ( member == null ){
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping
    public String homeV2( @SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                        Model model)
    {
        if ( loginMember == null ){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping
    public String homeV3ArgumentResolver( @Login Member loginMember,
                          Model model)
    {
        if ( loginMember == null ){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
