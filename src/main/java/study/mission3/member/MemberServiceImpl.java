package study.mission3.member;

public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository ;

    // 생성자 주입
    public MemberServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    @Override
    public void join(Member member){
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    // 스프링 컨테이너의 싱글톤 빈 보장 확인용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

}
