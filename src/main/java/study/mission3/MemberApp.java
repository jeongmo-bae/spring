package study.mission3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import study.mission3.member.*;

public class MemberApp {

    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        MemberService memberService = new MemberServiceImpl(memberRepository);

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService",MemberService.class);
        Member member1 = new Member(1L,"member1",Grade.BASIC);
        memberService.join(member1);

        System.out.println(memberService.findMember(member1.getId()).getId());
        System.out.println(memberService.findMember(member1.getId()).getName());
        System.out.println(memberService.findMember(member1.getId()).getGrade());

    }
}
