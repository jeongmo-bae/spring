package study.db.jdbc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.db.jdbc.domain.Member;
import study.db.jdbc.repository.MemberRepository;
import study.db.jdbc.repository.MemberRepositoryV1;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepository memberRepository;


//    // @RequiredArgsConstructor 로 자동생성 되므로 삭제
//    @Autowired
//    MemberServiceV1(MemberRepositoryV1 memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId).orElse(null);
        Member toMember = memberRepository.findById(toId).orElse(null);
        if (fromMember != null && toMember != null) {
            memberRepository.update(fromId, fromMember.getMoney() - money);
            validation(toMember);
            memberRepository.update(toId, toMember.getMoney() + money);
        }
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

}
