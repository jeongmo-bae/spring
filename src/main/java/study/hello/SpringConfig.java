package study.hello;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.hello.repository.MemberRepository;
import study.hello.repository.MemoryMemberRepository;
import study.hello.service.MemberService;

@Configuration
public class SpringConfig {
    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
