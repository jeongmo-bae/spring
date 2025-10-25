package study.core.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.core.discount.DiscountPolicy;
import study.core.member.MemberRepository;

@Component
@RequiredArgsConstructor
@Getter
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

//    @Autowired
//    public OrderServiceImpl(MemberRepository memberRepository,
////                            @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy){
//                            @MainDiscountPolicy DiscountPolicy discountPolicy){
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        int discountPrice = discountPolicy.discount(
            memberRepository.findById(memberId).orElse(null)
            , itemPrice
        );
        
        return new Order(memberId,itemName,itemPrice,discountPrice);
    }

    // 스프링 컨테이너의 싱글톤 빈 보장 확인용  - @Getter 활용으로 대체
//    public MemberRepository getMemberRepository() {
//        return memberRepository;
//    }
}
