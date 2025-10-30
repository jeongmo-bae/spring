package study.db.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.db.jdbc.domain.Member;

import javax.sql.DataSource;
import java.util.NoSuchElementException;


@Slf4j
@SpringBootTest
class MemberRepositoryV1Test {
    MemberRepositoryV1 repository;
    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/project_spring_db_study?serverTimezone=UTC");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");
        dataSource.setPoolName("MyHikariPoolTest");
        dataSource.setMaximumPoolSize(5);

        repository = new MemberRepositoryV1(dataSource);
        repository.delete("testId");
    }

    @Test
    @DisplayName("CRUD Test")
    void crud() {
        log.info("start");

        // given
        Member member = new Member("testId",1234);

        // when - then
        // save
        repository.save(member);
        Assertions.assertThat(member).isEqualTo(repository.findById(member.getMemberId()).orElse(null));
        // findById
        Member findMember = repository.findById(member.getMemberId()).orElse(null);
        Assertions.assertThat(member).isEqualTo(findMember);
//        Assertions.assertThatThrownBy(() -> repository.findById("notExistId")).isInstanceOf(NoSuchElementException.class);

        // update
        if (findMember != null) {
            findMember.setMoney(7777);
            repository.update(findMember);
        }

        // delete
        if (findMember != null) {
            repository.delete(findMember.getMemberId());
            Assertions.assertThat(repository.findById(findMember.getMemberId()).orElse(null)).isNull();
        }
    }
}