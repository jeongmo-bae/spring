package study.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        // default AutoAppConfig 가 속한 패키지가 base가 됨. 아래와 같은거지
        basePackages = "study.core",
        // AppConfig 스캔 빼기 위해
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
