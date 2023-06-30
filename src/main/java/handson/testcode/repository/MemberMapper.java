package handson.testcode.repository;

import handson.testcode.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    void save(Member member);

    Optional<Member> findById(int id);

    Optional<Member> findByEmail(String email);
}
