package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기 전용 JPA 성능 최적화
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 회원가입일 때 중복회원 검증
        memberRepository.save(member); // 저장 // save 함수에서 em.persist 를 하게되면 순간에 영속성 컨테스트에 Member 객체를 올리면, id 값이 키가 되고, id 값이 보장이된다.
        return member.getId(); // 보장된 id 값 반환
    }

    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    // @Transactional(readOnly = true)  // class 위에 선언이 되어있으면 주석처리해도 됨
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 단 건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name); // 변경감지
    }
}
