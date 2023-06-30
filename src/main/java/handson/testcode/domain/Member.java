package handson.testcode.domain;

import lombok.Getter;

@Getter
public class Member {

    private Integer id;
    private final String name;
    private final String email;

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
