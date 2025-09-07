package study.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import study.hello.domain.Member;
import study.hello.service.MemberService;


@Controller // Spring 뜰때, Spring 컨테이너에 넣어두고 관리함 -> Spring Container 에서 SpringBean 이 관리된다고 말함
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm(){
        return "/members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form){
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }

//    @GetMapping("/members")
//    회원관리 예제 진행 중

}
