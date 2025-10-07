package study.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class SingletonWithPrototypeTest1 {
    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addcount();
        prototypeBean2.addcount();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        System.out.println("count1 = " + count1);
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        System.out.println("count2 = " + count2);
//        Assertions.assertThat(count2).isEqualTo(2);
        Assertions.assertThat(count2).isEqualTo(1);
    }

    @Component
    @Scope("singleton")
    static class ClientBean{
//        private final PrototypeBean prototypeBean; // 생성시점에 주입되어 고정. 참조값 넘겨 받으니까

//        @Autowired
//        private ObjectProvider<PrototypeBean> prototypeBeanProvider;
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;


//        @Autowired
//        ClientBean(PrototypeBean prototypeBean) {
//            this.prototypeBean = prototypeBean;
//        }

        int logic(){
//            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addcount();
            return prototypeBean.getCount();
        }

    }

    @Scope("prototype")
    static class PrototypeBean{
        private int count = 0 ;

        void addcount(){
            this.count += 1;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        void init(){
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
