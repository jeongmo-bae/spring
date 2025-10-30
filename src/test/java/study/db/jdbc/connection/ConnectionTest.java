package study.db.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static study.db.jdbc.connection.ConnectionConst.*;

@Slf4j
@SpringBootTest
public class ConnectionTest {

    private DataSource dataSource ;
    @Autowired
    public ConnectionTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Test
    @DisplayName("DataSourceWithDriverManager")
    void driverManager() throws SQLException {
        // given
        Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
        log.info("conn={}",conn);
        // when

        // then
    }

    @Test
    @DisplayName("DataSourceWithDriverManager")
    void dataSourceDriverManager() throws SQLException{
        // DriverManagerDataSource - 항상 새로운 커넥션 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    @DisplayName("DataSourceWithHikariCP")
    void dataSourceConnectionPool() throws SQLException, InterruptedException{
        // 커넥션 풀링 : hikariProxyConnection(Proxy) -> JdbcConnection(Target)
        HikariDataSource userDefineDataSource = new HikariDataSource();
        userDefineDataSource.setJdbcUrl(URL);
        userDefineDataSource.setUsername(USER);
        userDefineDataSource.setPassword(PASSWORD);
        userDefineDataSource.setMaximumPoolSize(10);
        userDefineDataSource.setPoolName("MyPoolTest");
        useDataSource(userDefineDataSource);

        Thread.sleep(1000); // 커넥션 풀에서 커넥션 생성 시간 대기
    }

    @Test
    @DisplayName("DataSourceWithSpringHikariCP")
    void dataSourceConnectionPoolWithSpring() throws SQLException, InterruptedException{
        // given
        useDataSource(dataSource);
        Thread.sleep(1000);
        // when

        // then
    }


    private void useDataSource(DataSource dataSource) throws SQLException{
        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();
        log.info("###connection={}, class={}",conn1, conn1.getClass());
        log.info("###connection={}, class={}",conn2, conn2.getClass());
    }
}
