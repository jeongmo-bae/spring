package study.db.jdbc.service;

import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import study.db.jdbc.domain.Member;
import study.db.jdbc.repository.MemberRepository;
import study.db.jdbc.repository.MemberRepositoryV1;

import javax.sql.DataSource;

import java.sql.SQLException;

import static study.db.jdbc.connection.ConnectionConst.*;

//@SpringBootTest
class MemberServiceV1Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberServiceV1 memberServiceV1;
    private MemberRepositoryV1 memberRepository;
    private static DataSource dataSource;

    @BeforeAll
    static void beforeAll() {
        dataSource = getDataSource();  // 한 번만 생성
        System.out.println("dataSource 정의 완료. 커넥션풀 생성 대기");
    }

    @BeforeEach
    void beforeEach() {
        memberRepository = new MemberRepositoryV1(dataSource);
        memberServiceV1 = new MemberServiceV1(memberRepository);
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
        memberServiceV1.accountTransfer(MEMBER_A,MEMBER_B,2000);
        // then
        Member foundMemberA = memberRepository.findById(MEMBER_A).orElseThrow(() -> new IllegalStateException(MEMBER_A +" is not exists"));
        Member foundMemberB = memberRepository.findById(MEMBER_B).orElseThrow(() -> new IllegalStateException(MEMBER_B +" is not exists"));
        Assertions.assertThat(foundMemberA.getMoney()).isEqualTo(memberA.getMoney() - 2000);
        Assertions.assertThat(foundMemberB.getMoney()).isEqualTo(memberA.getMoney() + 2000);
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
            memberServiceV1.accountTransfer(MEMBER_A, MEMBER_EX, 2000);
        }catch (IllegalStateException e){
            // then
            System.out.println("e = " + e);
            Member foundMemberA = memberRepository.findById(MEMBER_A).orElseThrow(() -> new IllegalStateException(MEMBER_A +" is not exists"));
            Member foundMemberEx = memberRepository.findById(MEMBER_EX).orElseThrow(() -> new IllegalStateException(MEMBER_EX +" is not exists"));

            System.out.println("foundMember.getMoney() = " + foundMemberA.getMoney());
            System.out.println("foundMemberEx.getMoney() = " + foundMemberEx.getMoney());

            Assertions.assertThat(foundMemberA.getMoney()).isEqualTo(memberA.getMoney() - 2000);
            Assertions.assertThat(foundMemberEx.getMoney()).isEqualTo(memberEx.getMoney());
        }
    }


    private static DataSource getDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USER);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setMaximumPoolSize(5);
        hikariDataSource.setPoolName("MyHikariCP");
        return hikariDataSource;
    }

}