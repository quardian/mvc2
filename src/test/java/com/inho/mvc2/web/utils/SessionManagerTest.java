package com.inho.mvc2.web.utils;

import com.inho.mvc2.domain.model.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SessionManagerTest {

    SessionManager sm = new SessionManager();

    @Test
    void sessionTest()
    {
        // 세션 생성
        Member member = new Member("test", "이인호", "1111");
        MockHttpServletResponse response = new MockHttpServletResponse();
        sm.createSession(member, response);


        // 요청 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        // 세션 조회
        Object session = sm.getSession(request);

        Assertions.assertThat(session).isEqualTo(member);

        // 세션 만료
        sm.expire(request);

        Object expired = sm.getSession(request);
        Assertions.assertThat(expired).isNull();

    }
}