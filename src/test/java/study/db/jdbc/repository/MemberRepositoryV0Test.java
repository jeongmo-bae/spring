package study.db.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.db.jdbc.domain.Member;

import java.sql.*;
import java.util.NoSuchElementException;

import static study.db.jdbc.connection.ConnectionConst.*;

@Slf4j
class MemberRepositoryV0Test {
    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();
    private String testMemberId = "testId";
    private int testMoney = 9999;

    @BeforeEach
    void beforeEach() {
        log.info("beforeEach");
        String sql = "delete from project_spring_db_study.member ";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            pstmt = conn.prepareStatement(sql);
            int deletedCnt = pstmt.executeUpdate();
            log.info("deletedCnt = {}", deletedCnt);
        } catch (SQLException e) {
            log.error("SQLException", e);
        }
    }

    @Test
    void saveTest() throws Exception {
        log.info("saveTest");
        // given
        Member member = new Member(testMemberId, testMoney);
        memberRepositoryV0.save(member);

        // when
//        String sql = "select " +
//                "member_id, money " +
//                "from project_spring_db_study.member where member_id = ?";
//        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        pstmt.setString(1, member.getMemberId());
//        ResultSet rs = pstmt.executeQuery();
        Member selectedMember = memberRepositoryV0.findById(member.getMemberId()).orElse(null);

        // then
//        int count = 0;
//        while (rs.next()) {
//            Assertions.assertThat(rs.getString(1)).isEqualTo(member.getMemberId());
//            Assertions.assertThat(rs.getInt(2)).isEqualTo(member.getMoney());
//            count++;
//        }
//        Assertions.assertThat(count).isEqualTo(1);
        Assertions.assertThat(selectedMember).isEqualTo(member);
        log.info("selectedMember = {}", selectedMember);
        log.info("member = {}", member);
    }

    @Test
     void findNotExistIdTest() {
        log.info("findByIdNotFoundTest");
        // given
        String notExistId = "notExistId";

        // when
        Member member = memberRepositoryV0.findById(notExistId).orElse(null);

        // then
        Assertions.assertThat(member).isNull();
    }

    @Test
    void findByIdThrowsExceptionWhenNotFoundTest() {
        log.info("findByIdThrowsExceptionWhenNotFoundTest");
        // given
        String notExistId = "notExistId";

        // when

        // then
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> {
            memberRepositoryV0.findById(notExistId)
                    .orElseThrow(()->new NoSuchElementException("not found"));
        });
    }

    @Test
    @DisplayName("update Test")
    void updateTest() {
        // given
        Member member = new Member("jungmo", 12345);
        memberRepositoryV0.save(member);

        // when
        member.setMoney(999999);
        memberRepositoryV0.update(member);
        Member findMember = memberRepositoryV0.findById(member.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("not found"));

        // then
        Assertions.assertThat(member.getMoney()).isEqualTo(findMember.getMoney());
    }

    @Test
    @DisplayName("delete test")
    void deleteTest() {
        // given
        Member member = new Member("jungmo", 12345);
        if (!memberRepositoryV0.findById(member.getMemberId()).isPresent()) {
            memberRepositoryV0.save(member);
        }

        // when
        memberRepositoryV0.delete(member.getMemberId());

        // then
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,() -> {
            memberRepositoryV0.findById(member.getMemberId())
                    .orElseThrow(()-> new NoSuchElementException("not found"));
        });
    }
}