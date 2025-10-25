package study.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;


class StatefulServiceTest {

    @Test
    @DisplayName("stateful singleton test")
    void statefulServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        // Thread A : 10000원 주문
        int userAOrderPrice = statefulService1.order("userA",10000);
        // Thread B : 20000원 주문
        int userBOrderPrice = statefulService2.order("userB",20000);

        //Tread A : 주문 금액 조회
//        System.out.println("price = " + statefulService1.getPrice());
//        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(10000);
        Assertions.assertThat(userAOrderPrice).isEqualTo(10000);
        Assertions.assertThat(userBOrderPrice).isEqualTo(20000);

    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}