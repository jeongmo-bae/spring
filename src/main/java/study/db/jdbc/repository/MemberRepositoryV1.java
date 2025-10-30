package study.db.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import study.db.jdbc.connection.DBConnectionUtil;
import study.db.jdbc.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

/**
 * ConnectionPool 사용 - HikariCP
 */
@Slf4j
@Repository
public class MemberRepositoryV1 implements MemberRepository{

    private final DataSource dataSource;

    @Autowired
    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into project_spring_db_study.member(member_id,money) values(?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("save error",e);
            throw new IllegalStateException(e);
        } finally {
            close(conn,pstmt,null);
        }
    }

    @Override
    public Optional<Member> findById(String memberId) {
        String sql = "select member_id,\n" +
                "       money\n" +
                "from project_spring_db_study.member\n" +
                "where member_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memberId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return Optional.of(member);
            } else {
//                throw new NoSuchElementException("member not found memberId=" + memberId);
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public void update(Member member) {
        String sql = "update project_spring_db_study.member set money = ? where member_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,member.getMoney());
            pstmt.setString(2,member.getMemberId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("update error",e);
        } finally {
//            close(conn,pstmt,null);
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from project_spring_db_study.member where member_id = ? ";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
//            close(conn,pstmt,null);
        }


    }

    private void close(Connection conn, Statement stmt, ResultSet rs){
        JdbcUtils.closeConnection(conn);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeResultSet(rs);
    }

//    private Connection getConnection(){
//        return DBConnectionUtil.getConnection();
//    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("get connection={}, class={}",connection,connection.getClass());
        return connection;
    }
}
