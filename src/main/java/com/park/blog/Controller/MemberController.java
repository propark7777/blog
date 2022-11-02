package com.park.blog.Controller;

import com.park.blog.domain.member.Member;
import com.park.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/join")
    public String addForm(@ModelAttribute("member")Member member) {
        return "members/addMemberForm";
    }
}
