package io.ssosso.springdatajpa.controller;

import io.ssosso.springdatajpa.dto.MemberDto;
import io.ssosso.springdatajpa.entity.Member;
import io.ssosso.springdatajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberRepository memberRepository;

  @GetMapping("/members/{id}")
  public String findMember(@PathVariable("id") Long id) {
    Member member = memberRepository.findById(id).get();
    return member.getUsername();
  }

  @GetMapping("/members2/{id}")
  public String findMember2(@PathVariable("id") Member member) {
    return member.getUsername();
  }

  @GetMapping("/members")
  private Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {
    return memberRepository.findAll(pageable);
  }

  @GetMapping("/members2")
  private Page<MemberDto> list2(@PageableDefault(size = 5) Pageable pageable) {
//    PageRequest.of(0, 20);
    Page<Member> page = memberRepository.findAll(pageable);
    return page.map(MemberDto::new);
  }

  @PostConstruct
  public void init() {
    for (int i = 0; i < 100; i++) {
      memberRepository.save(new Member("user" + i, i));
    }
  }
}
