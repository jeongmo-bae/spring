package study.db.jdbc.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class Member {
    private String memberId;
    private int money;

    public Member(){
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }

//      //Equals & HashCode
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Member member = (Member) o;
//        return money == member.money && Objects.equals(memberId, member.memberId);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof Member member)) return false;
//        return money == member.money && Objects.equals(memberId, member.memberId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(memberId, money);
//    }
}
