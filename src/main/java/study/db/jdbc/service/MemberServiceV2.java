package study.db.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import study.db.jdbc.domain.Member;
import study.db.jdbc.repository.MemberRepository;
import study.db.jdbc.repository.MemberRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;


//    // @RequiredArgsConstructor 로 자동생성 되므로 삭제
//    @Autowired
//    MemberServiceV1(DataSource dataSource, MemberRepositoryV1 memberRepository) {
//        this.dataSource = dataSource;
//        this.memberRepository = memberRepository;
//    }
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection conn = dataSource.getConnection();
        try{
            conn.setAutoCommit(false);
            bizLogic(conn, fromId, toId, money);
            conn.commit();
        }catch (Exception e){
            conn.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(conn);
        }
        
    }

    private void bizLogic(Connection conn, String fromId , String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(conn,fromId).orElse(null);
        Member toMember = memberRepository.findById(conn,toId).orElse(null);
        if (fromMember != null && toMember != null) {
            memberRepository.update(conn,fromId, fromMember.getMoney() - money);
            validation(toMember);
            memberRepository.update(conn,toId, toMember.getMoney() + money);
        }
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    private void release(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); //커넥션 풀 고려
                conn.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

}
