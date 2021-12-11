package com.inho.mvc2.domain.service.login;

import com.inho.mvc2.domain.model.member.Member;
import com.inho.mvc2.domain.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId, String password)
    {
        return memberRepository.findByLoginId(loginId)
                .filter( m-> m.getPassword().equals(password) )
                .orElse(null );
        /*
            Optional<Member> findMember = memberRepository.findByLoginId(loginId);
            if (! findMember.isPresent() ) return null;
            Member member = findMember.get();
            if ( member.getPassword().equals(password)){
                return member;
            }
            return null;
         */
    }
}
