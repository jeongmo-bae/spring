package examples.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BoardApplication {

    private static final Logger log = LoggerFactory.getLogger(BoardApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);
    }
}
