package com.example.demo.member;

import com.example.demo.domain.Member;
import com.example.demo.domain.Study;

import java.util.Optional;

public interface MemberService {
  Optional<Member> findById(Long memberId) throws MemberNotFoundException;

  void validate(long l);

  void notify(Study save);

  void notify(Member member);
}
