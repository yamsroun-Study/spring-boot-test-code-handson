package handson.testcode.repository;

import handson.testcode.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper memberMapper;

    public void save(Member member) {
        memberMapper.save(member);
    }

    public Optional<Member> findById(int id) {
        return memberMapper.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }
}
