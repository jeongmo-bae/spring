package study.mission3.order;

import study.mission3.discount.DiscountPolicy;
import study.mission3.member.MemberRepository;

public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

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

}
