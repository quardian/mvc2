package com.inho.mvc2.web.controller.login;

import com.inho.mvc2.domain.model.member.Member;
import com.inho.mvc2.domain.service.login.LoginService;
import com.inho.mvc2.web.model.SessionConst;
import com.inho.mvc2.web.model.form.login.LoginForm;
import com.inho.mvc2.web.utils.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor

public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    /**
     * 로그입 입력 폼
     * @param model
     * @param form
     * @return
     */
    @GetMapping("/login")
    public String loginForm(Model model,
                            @ModelAttribute("loginForm") LoginForm form)
    {
        return "login/login";
    }

    //@PostMapping("/login")
    public String login(Model model,
                        @Validated  @ModelAttribute("loginForm") LoginForm form,
                        @RequestParam(value="redirectURL", defaultValue = "/") String redirectURL,
                        BindingResult bindingResult,
                        HttpServletResponse response)
    {
        String loginViewName ="login/login";

        if ( bindingResult.hasErrors() ) {
            return loginViewName;
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if ( loginMember == null ){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않습니다.");
            return loginViewName;
        }

        // 로그인 성공 처리 ( 쿠키 시간 정보를 주지 않아 세션 쿠키 : 브라우저 닫으면 없어짐 )
        //Cookie loginCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        //response.addCookie(loginCookie);
        sessionManager.createSession(loginMember, response);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/login")
    public String loginV2(Model model,
                        HttpSession session,
                          @RequestParam(value="redirectURL", defaultValue = "/") String redirectURL,
                        @Validated  @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult,
                        HttpServletResponse response)
    {
        String loginViewName ="login/login";

        if ( bindingResult.hasErrors() ) {
            return loginViewName;
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if ( loginMember == null ){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않습니다.");
            return loginViewName;
        }

        // 로그인 성공 처리 ( 쿠키 시간 정보를 주지 않아 세션 쿠키 : 브라우저 닫으면 없어짐 )
        if ( "test".equals(loginMember.getLoginId()) ){
            session.setMaxInactiveInterval(100); // test 계정은 100초만 세션 유지 ( 특정 계정만 세션 시간 다르게 갖어갈 떄.. )
        }
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
    }



    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response)
    {
        //expireCookie(response, "memberId");
        //sessionManager.expire(request);
        HttpSession session = request.getSession( false);
        if ( session != null ) {
            session.invalidate();
            //session.removeAttribute(SessionConst.LOGIN_MEMBER);
        }

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie loginCookie = new Cookie(cookieName,  null);
        loginCookie.setMaxAge(0);
        response.addCookie(loginCookie);
    }

}
