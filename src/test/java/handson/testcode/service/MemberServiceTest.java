package handson.testcode.service;

import handson.testcode.domain.Member;
import handson.testcode.exception.MemberException;
import handson.testcode.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입 - 정상")
    void join() {
        //when
        Member member = new Member("재진", "jj@ab.com");
        memberService.join(member);

        //then
        then(memberRepository).should().save(member);
    }

    @Test
    @DisplayName("회원 가입 - 이메일 중복")
    void join_duplicateEmail() {
        //given
        Member member1 = new Member("재진", "jj@ab.com");
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member1));

        //when
        Member member = new Member("재진", "jj@ab.com");
        ThrowableAssert.ThrowingCallable callable = () -> memberService.join(member);

        //then
        assertThatThrownBy(callable).isInstanceOf(MemberException.class);
        then(memberRepository).should(never()).save(member);
    }

    @Test
    @DisplayName("회원 조회 - 결과 있음")
    void getMember() {
        //given
        Member member = new Member("재진", "jj@ab.com");
        given(memberRepository.findById(anyInt())).willReturn(Optional.of(member));

        //when
        int memberId = 1;
        Member result = memberService.getMember(memberId);

        //then
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 조회 - 결과 없음")
    void getMember_noExists() {
        //given
        given(memberRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        int memberId = -999;
        ThrowableAssert.ThrowingCallable callable = () -> memberService.getMember(memberId);

        //then
        Assertions.assertThatThrownBy(callable).isInstanceOf(MemberException.class);
    }
}
