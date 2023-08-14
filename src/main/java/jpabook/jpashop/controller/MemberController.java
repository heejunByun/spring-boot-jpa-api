package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); // 빈 껍데기를 가져가는 이유는 Validation 을 해준다?
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        // 어노테이션
        // @Valid: MemberForm form 의 Validation 을 진행한다.
        // @BindingResult: 오류가 발생하면, 오류가 튕겨버리는데, @Valid 한 후 BindingResult 가 있으면 오류가 result 에 담겨서 코드가 실행된다.

        if (result.hasErrors()) { // 오류가 있으면
            // thymeleaf-spring library 가 spring 관련 library 와 인티그레이션이 잘 되어있다.
            // spring 이 createMemberForm 오류들을 다 가져가준다. > (createMemberForm 에서 message 와 빨간색 테두리까지 그려준다)
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member); // 회원 저장

        // 저장 후 리턴 경로 > 저장이 되고 재 로딩이 되면 좋지 않기 떄문에 redirect 를 많이 사용한다
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
        /**
         *
         *  참고: 폼 객체 vs 엔티티 직접 사용
         *   > 참고: 요구사항이 정말 단순할 때는 폼 객체( MemberForm ) 없이 엔티티( Member )를 직접 등록과 수정
         *   화면에서 사용해도 된다. 하지만 화면 요구사항이 복잡해지기 시작하면, 엔티티에 화면을 처리하기 위한
         *   기능이 점점 증가한다. 결과적으로 엔티티는 점점 화면에 종속적으로 변하고, 이렇게 화면 기능 때문에
         *   지저분해진 엔티티는 결국 유지보수하기 어려워진다.
         *   > 실무에서 엔티티는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 한다. 화면이나 API에
         *   맞는 폼 객체나 DTO를 사용하자. 그래서 화면이나 API 요구사항을 이것들로 처리하고, 엔티티는 최대한
         *   순수하게 유지하자.
         */
    }
}
