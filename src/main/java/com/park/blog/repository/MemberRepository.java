package com.park.blog.repository;

import com.park.blog.domain.member.Member;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MemberRepository {
    private final JdbcTemplate template;

    public MemberRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public Member saveMember(Member member) {
        String sql = "insert into member(id,name,password,email) values(?,?,?,?)";
        template.update(sql,member.getId(),member.getName(),member.getPassword(),member.getEmail());
        return member;
    }
}
