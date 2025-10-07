package study.mission3.discount;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import study.mission3.member.Grade;
import study.mission3.member.Member;

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy{
    private int fixedDiscountAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade()== Grade.VIP){
            return fixedDiscountAmount;
        } else{
            return 0;
        }
    }
}
