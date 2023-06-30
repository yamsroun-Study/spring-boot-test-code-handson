package handson.testcode.service;

import handson.testcode.domain.Member;
import handson.testcode.exception.MemberException;
import handson.testcode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void join(Member member) {
        validateBeforeJoin(member);
        memberRepository.save(member);
    }

    private void validateBeforeJoin(Member member) {
        Optional<Member> existsMember = memberRepository.findByEmail(member.getEmail());
        if (existsMember.isPresent()) {
            throw new MemberException("이미 등록된 이메일입니다.");
        }
    }

    public Member getMember(int id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new MemberException("회원 정보가 없습니다."));
    }
}
