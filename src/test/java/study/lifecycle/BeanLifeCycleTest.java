package study.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {
    @Test
    void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
//        NetworkClient client = ac.getBean(NetworkClient.class);
//        NetworkClientV2 client = ac.getBean(NetworkClientV2.class);
        NetworkClientV3 client = ac.getBean(NetworkClientV3.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig{
//        @Bean
//        public NetworkClient networkClient(){
//            NetworkClient networkClient = new NetworkClient();
//            networkClient.setUrl("http://hello-spring.dev");
//            return networkClient;
//        }

//        @Bean(initMethod = "init",destroyMethod = "close")
//        public NetworkClientV2 networkClientV2(){
//            NetworkClientV2 networkClientV2 = new NetworkClientV2();
//            networkClientV2.setUrl("http://hello-spring.dev");
//            return networkClientV2;
//        }

        @Bean
        public NetworkClientV3 networkClientV3(){
            NetworkClientV3 networkClientV3 = new NetworkClientV3();
            networkClientV3.setUrl("http://hello-spring.dev");
            return networkClientV3;
        }


    }
}
