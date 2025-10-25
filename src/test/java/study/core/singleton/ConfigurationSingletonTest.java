package study.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import study.core.AppConfig;
import study.core.member.MemberRepository;
import study.core.member.MemberService;
import study.core.member.MemberServiceImpl;
import study.core.order.OrderService;
import study.core.order.OrderServiceImpl;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        OrderService orderService = ac.getBean("orderService", OrderService.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = ((MemberServiceImpl) memberService).getMemberRepository();
        MemberRepository memberRepository2 = ((OrderServiceImpl) orderService).getMemberRepository();

        System.out.println("memberRepository = " + memberRepository);
        System.out.println("memberService.getMemberRepository = " + memberRepository1);
        System.out.println("orderService.getMemberRepository= " + memberRepository2);

        Assertions.assertThat(memberRepository).isSameAs(memberRepository1);
        Assertions.assertThat(memberRepository1).isSameAs(memberRepository2);
    }

    @Test
    void configurateionDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean.getClass());
//        bean = class study.mission3.AppConfig$$SpringCGLIB$$0
    }
}
