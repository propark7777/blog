package com.park.blog.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long member_num;
    private String id;
    private String name;
    private String password;
    private String email;

    public Member() {
    }

    public Member(String id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
