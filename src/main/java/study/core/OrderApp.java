package study.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import study.core.member.Grade;
import study.core.member.Member;
import study.core.member.MemberService;
import study.core.order.Order;
import study.core.order.OrderService;

public class OrderApp {
//    private static final MemberRepository memberRepository = new MemoryMemberRepository();
//    private static final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//    private static final MemberService memberService = new MemberServiceImpl(memberRepository);
//    private static final OrderService orderService = new OrderServiceImpl(memberRepository, discountPolicy);


    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService",MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService",OrderService.class);

        Member member1 = new Member(1L,"member1",Grade.VIP);
        memberService.join(member1);

        Order order = orderService.createOrder(1L, "itemA", 10000);
        System.out.println("order = " + order.toString());
    }

}
