package study.db.jdbc.repository;

import study.db.jdbc.domain.Member;

import java.sql.Connection;
import java.util.Optional;

public interface MemberRepositoryForTransaction {
    Member save(Member member);
    Optional<Member> findById(Connection conn, String memberId);
    void update(Connection conn, String memberId, int money);
    void delete(String memberId);
}
