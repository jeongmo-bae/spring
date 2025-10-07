package study.mission3.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.mission3.discount.DiscountPolicy;
import study.mission3.member.MemberRepository;

@Component
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy){
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        int discountPrice = discountPolicy.discount(
            memberRepository.findById(memberId).orElse(null)
            , itemPrice
        );
        
        return new Order(memberId,itemName,itemPrice,discountPrice);
    }

    // 스프링 컨테이너의 싱글톤 빈 보장 확인용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
