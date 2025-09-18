package study.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// import study.hello.domain.Member;
// import study.hello.service.MemberService;

@Controller
public class AccountController {
    // private final MemberService memberService;

    // @Autowired
    // public MemberController(MemberService memberService){
    //     this.memberService = memberService;
    // }

    @GetMapping("/accounts/new")
    public String createForm(){
        return "/accounts/createAccountForm";
    }

    // @PostMapping("/members/new")
    // public String create(MemberForm form){
    //     Member member = new Member();
    //     member.setName(form.getName());
    //     memberService.join(member);
    //     return "redirect:/";
    // }
}