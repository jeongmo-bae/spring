package study.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.core.discount.DiscountPolicy;
import study.core.discount.RateDiscountPolicy;
import study.core.member.MemberRepository;
import study.core.member.MemberService;
import study.core.member.MemberServiceImpl;
import study.core.member.MemoryMemberRepository;
import study.core.order.OrderService;
import study.core.order.OrderServiceImpl;

@Configuration
public class AppConfig {
//    private final MemberRepository memberRepository = new MemoryMemberRepository();
    @Bean
    public MemberService memberService(){
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public OrderService orderService(){
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(),discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy(){
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    @Bean
    public MemberRepository memberRepository(){
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }
}
