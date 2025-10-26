package study.db.jdbc.repository;

import study.db.jdbc.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(String memberId);
    void update(Member member);
    void delete(String memberId);
}
