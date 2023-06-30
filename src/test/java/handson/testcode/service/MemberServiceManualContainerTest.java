package handson.testcode.service;

import handson.testcode.domain.Member;
import handson.testcode.exception.MemberException;
import handson.testcode.repository.MemberRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceManualContainerTest {

    private static PostgreSQLContainer<?> POSTGRES_CONTAINER;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeAll
    static void setUp() {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:13")
            .withInitScript("member-init.sql");
        POSTGRES_CONTAINER.start();

        //System.setProperty("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        //System.setProperty("spring.datasource.username", POSTGRES_CONTAINER.getUsername());
        //System.setProperty("spring.datasource.password", POSTGRES_CONTAINER.getPassword());
    }

    @AfterAll
    static void tearDown() {
        POSTGRES_CONTAINER.stop();
    }

    @Test
    @DisplayName("회원 가입 - 정상")
    void join() {
        //when
        Member member = new Member("재진", "jj@ab.com");
        memberService.join(member);

        //then
        assertThat(member.getId()).isPositive();
        Optional<Member> result = memberRepository.findById(member.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(member.getName());
        assertThat(result.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 가입 - 이메일 중복")
    void join_duplicateEmail() {
        //given
        Member member1 = new Member("임재진", "jj@ab.com");
        memberService.join(member1);

        //when
        Member member = new Member("재진", "jj@ab.com");
        ThrowableAssert.ThrowingCallable callable = () -> memberService.join(member);

        //then
        assertThatThrownBy(callable).isInstanceOf(MemberException.class);
    }

    @Test
    @DisplayName("회원 조회 - 결과 있음")
    void getMember() {
        //given
        Member member = new Member("재진", "jj@ab.com");
        memberService.join(member);

        //when
        Member result = memberService.getMember(member.getId());

        //then
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 조회 - 결과 없음")
    void getMember_noExists() {
        //when
        int memberId = -999;
        ThrowableAssert.ThrowingCallable callable = () -> memberService.getMember(memberId);

        //then
        assertThatThrownBy(callable).isInstanceOf(MemberException.class);
    }
}

