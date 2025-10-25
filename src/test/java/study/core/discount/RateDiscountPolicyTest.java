package study.core.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.core.member.Grade;
import study.core.member.Member;

class RateDiscountPolicyTest {
    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip_o(){
        RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();
        Member member = new Member(1L,"member1", Grade.VIP);
        int discountPrice = rateDiscountPolicy.discount(member,10000);

        Assertions.assertThat(discountPrice).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_x(){
        RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();
        Member member = new Member(1L,"member1", Grade.BASIC);
        int discountPrice = rateDiscountPolicy.discount(member,10000);

        Assertions.assertThat(discountPrice).isEqualTo(0);
    }


}