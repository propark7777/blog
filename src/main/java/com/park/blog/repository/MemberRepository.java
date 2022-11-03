package com.park.blog.repository;

import com.park.blog.domain.member.Member;
import com.park.blog.domain.plan.Plan;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    public Member findMemberById(String id) {
        String sql = "select * from member where id = ?";
        return template.queryForObject(sql,memberRowMapper(),id);
    }

    public List<Member> findAllMember() {
        String sql = "select * from member";
        return template.query(sql,memberRowMapper());
    }

    public void updateMember(String id, Member member) {
        String sql = "update member set name = ?, password = ?, email =? where id =?";
        template.update(sql,member.getName(),member.getPassword(),member.getEmail(),id);
    }

    public void deleteMember(String id) {
        String sql = "delete member where id =?";
        template.update(sql,id);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs,rowNum)-> {

            Member member = new Member();
            member.setId(rs.getString("id"));
            member.setName(rs.getString("name"));
            member.setPassword(rs.getString("password"));
            member.setEmail(rs.getString("email"));
            return member;
        };
    }
}
