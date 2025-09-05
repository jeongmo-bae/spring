package study.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import study.hello.service.MemberService;


@Controller // Spring 뜰때, Spring 컨테이너에 넣어두고 관리함 -> Spring Container 에서 SpringBean 이 관리된다고 말함
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }
}
