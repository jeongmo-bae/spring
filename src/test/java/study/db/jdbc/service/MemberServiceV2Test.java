package study.db.jdbc.service;


import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import study.db.jdbc.domain.Member;
import study.db.jdbc.repository.MemberRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static study.db.jdbc.connection.ConnectionConst.*;

class MemberServiceV2Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberServiceV2 memberServiceV2;
    private MemberRepositoryV2 memberRepository;
    private static DataSource dataSource;

    @BeforeAll
    static void setUp() throws SQLException {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USER);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setPoolName("MemberServiceV2Test");
        dataSource = hikariDataSource;
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        memberRepository = new MemberRepositoryV2(dataSource);
        memberServiceV2 = new MemberServiceV2(dataSource, memberRepository);
    }

    @AfterEach
    void afterEach() {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체 테스트")
    void accountTransfer() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A,10000);
        Member memberB = new Member(MEMBER_B,10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        // when
        memberServiceV2.accountTransfer(MEMBER_A,MEMBER_B,2000);
        // then
        Member foundMemberA = memberRepository.findById(dataSource.getConnection(),MEMBER_A).orElseThrow(() -> new IllegalStateException(MEMBER_A +" is not exists"));
        Member foundMemberB = memberRepository.findById(dataSource.getConnection(),MEMBER_B).orElseThrow(() -> new IllegalStateException(MEMBER_B +" is not exists"));
        org.assertj.core.api.Assertions.assertThat(foundMemberA.getMoney()).isEqualTo(memberA.getMoney() - 2000);
        org.assertj.core.api.Assertions.assertThat(foundMemberB.getMoney()).isEqualTo(memberA.getMoney() + 2000);
    }
    @Test
    @DisplayName("이체중 실패 케이스 테스트")
    void failAccountTransfer() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A,10000);
        Member memberEx = new Member(MEMBER_EX,10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);
        // when
        try {
            memberServiceV2.accountTransfer(MEMBER_A, MEMBER_EX, 2000);
        }catch (IllegalStateException e){
            // then
            System.out.println("e = " + e);
            Member foundMemberA = memberRepository.findById(dataSource.getConnection(),MEMBER_A).orElseThrow(() -> new IllegalStateException(MEMBER_A +" is not exists"));
            Member foundMemberEx = memberRepository.findById(dataSource.getConnection(),MEMBER_EX).orElseThrow(() -> new IllegalStateException(MEMBER_EX +" is not exists"));

            System.out.println("foundMember.getMoney() = " + foundMemberA.getMoney());
            System.out.println("foundMemberEx.getMoney() = " + foundMemberEx.getMoney());

            org.assertj.core.api.Assertions.assertThat(foundMemberA.getMoney()).isEqualTo(memberA.getMoney());
            Assertions.assertThat(foundMemberEx.getMoney()).isEqualTo(memberEx.getMoney());
        }
    }

}