package study.db.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdbcApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JdbcApplication.class);
        app.setAdditionalProfiles("spring-db-study");
        app.run(args);
    }
}
