package com.inho.mvc2.domain.repository.member;

import com.inho.mvc2.domain.model.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static final Map<Long, Member> store = new HashMap<>(); //static
    private static long sequence = 0L; //static


    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);

        log.info("save member : {}", member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }


    public Optional<Member> findByLoginId(String loginId)
    {
        return  findAll().stream()
                .filter( m->m.getLoginId().equals(loginId) )
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Member updateParam) {
        Member findMember = findById(itemId);

        findMember.setId(updateParam.getId());
        findMember.setLoginId(updateParam.getLoginId());
        findMember.setName(updateParam.getName());
        findMember.setPassword(updateParam.getPassword());
    }

    public void clearStore() {
        store.clear();
    }
}
