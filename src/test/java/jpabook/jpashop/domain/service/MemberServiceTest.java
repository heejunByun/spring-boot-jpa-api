package jpabook.jpashop.domain.service;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    // @Rollback(value = false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("byun");
        //when
        Long saveId = memberService.join(member);
        //then
        //Assert.assertEquals
        em.flush(); // insert 쿼리를 실행시킴
        assertEquals(member, memberRepository.findOne(saveId)); // 가능한 이유는 @Transactional
    }


    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("byun");
        Member member2 = new Member();
        member2.setName("byun");
        //when
        /**
         * memberService.join(member1);
         * try {
         *      memberService.join(member2);
         * } catch (IllegalStateException e) {
         *      //테스트 할 때 원하는 정상적인 에러가 터진다면 return 되기 때문에 성공으로 찍힌다.
         *      return;
         *  }
         */
        memberService.join(member1);
        memberService.join(member2);
        //then
        //Assert.fail();
        fail("예외가 발생해야 한다. (이 문구가 나오면 안된다.)"); // 테스트 코드를 잘못 작성하게되면, 성공으로 나온다. 무조건 fail 이 나오도록
    }
}