package study.mission3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.mission3.discount.DiscountPolicy;
import study.mission3.discount.RateDiscountPolicy;
import study.mission3.member.MemberRepository;
import study.mission3.member.MemberService;
import study.mission3.member.MemberServiceImpl;
import study.mission3.member.MemoryMemberRepository;
import study.mission3.order.OrderService;
import study.mission3.order.OrderServiceImpl;

@Configuration
public class AppConfig {
//    private final MemberRepository memberRepository = new MemoryMemberRepository();
    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(),discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy(){
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
