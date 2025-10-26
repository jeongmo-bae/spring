package study.db.jdbc.connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;


class DBConnectionUtilTest {
    @Test
    @DisplayName("DB Connection Test")
    void getConnectionTest() throws Exception {
        // given
        Connection connection = DBConnectionUtil.getConnection();
        // when

        // then
        Assertions.assertThat(connection).isNotNull();
    }
}