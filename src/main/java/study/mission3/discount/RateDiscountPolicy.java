package study.mission3.discount;

import study.mission3.member.Grade;
import study.mission3.member.Member;

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
