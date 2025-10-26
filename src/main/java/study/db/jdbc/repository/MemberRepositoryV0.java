package study.db.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import study.db.jdbc.connection.DBConnectionUtil;
import study.db.jdbc.domain.Member;

import java.sql.*;
import java.util.Optional;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 implements MemberRepository{

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
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from project_spring_db_study.member where member_id = ? ";
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void close(Connection conn, Statement stmt, ResultSet rs){
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("close error",e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("close error",e);
            }
        }

        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close error",e);
            }
        }
    }

    private Connection getConnection(){
        return DBConnectionUtil.getConnection();
    }
}
