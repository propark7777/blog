package com.park.blog.repository;

import com.park.blog.domain.member.Member;

public class MemberRepository {
    private static final MemberRepository instance = new MemberRepository();

    private static long sequence = 0L;

    public static MemberRepository getInstance() {
        return instance;
    }

    public MemberRepository() {

    }

    public Member saveMember(Member member) {
        member.setMember_num(++sequence);
        return member;
    }
}
