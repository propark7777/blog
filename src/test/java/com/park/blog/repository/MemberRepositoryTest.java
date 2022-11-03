package com.park.blog.repository;

import static com.park.blog.connection.ConnectionConst.PASSWORD;
import static com.park.blog.connection.ConnectionConst.URL;
import static com.park.blog.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.park.blog.domain.member.Member;
import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberRepositoryTest {
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        //기본 DriverManager
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        memberRepository = new MemberRepository(dataSource);
    }

    @Test
    void saveMember() {
        Member member = new Member("hahahagh1","park","1234","hahahagh@naver.com");
        Member saveMember = memberRepository.saveMember(member);
        assertThat(saveMember).isEqualTo(member);
    }

    @Test
    void deleteMember() {
        memberRepository.deleteMember("hahahagh1");
    }

    @Test
    void updateMember() {
        Member member = new Member("aaa","Lee","1234","vv@naver.com");
        memberRepository.updateMember("hahahagh",member);
    }
}