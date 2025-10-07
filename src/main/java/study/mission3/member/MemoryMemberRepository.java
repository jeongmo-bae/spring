package study.mission3.member;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MemoryMemberRepository implements MemberRepository {
    private Map<Long, Member> memberMap = new HashMap<>();

    @Override
    public void save(Member member){
        if(!memberMap.containsKey(member.getId())){
            memberMap.put(member.getId(), member); 
        }
    }
    @Override
    public Optional<Member> findById(Long id){
        return Optional.ofNullable(memberMap.get(id));
    }

    @Override
    public List<Member> findAll(){
        // List<Member> memberList = new ArrayList<>();
        // for (Map.Entry<String,Member> entry : memberMap.entrySet()) {
        //     memberList.append(entry.getValue());
        // }
        return memberMap.values().stream().toList(); 
    }
}
