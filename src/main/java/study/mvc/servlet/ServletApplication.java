package study.mvc.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // 서블릿 자동등록
@SpringBootApplication
public class ServletApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServletApplication.class);
        app.setAdditionalProfiles("spring-mvc-study-servlet");
        app.run(args);
    }
}
