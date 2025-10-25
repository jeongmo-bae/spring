package study.core.discount;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import study.core.member.Grade;
import study.core.member.Member;

@Component
//@Qualifier("mainDiscountPolicy")
//@MainDiscountPolicy
@Primary
public class RateDiscountPolicy implements DiscountPolicy{
    private final double discountRate = 0.1;
    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return (int) (price * discountRate);
        }
        return 0;
    }
}
