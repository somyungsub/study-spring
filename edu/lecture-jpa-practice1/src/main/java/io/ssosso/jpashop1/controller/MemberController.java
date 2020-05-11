package io.ssosso.jpashop1.controller;

import io.ssosso.jpashop1.domain.Address;
import io.ssosso.jpashop1.domain.Member;
import io.ssosso.jpashop1.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberForm());
    return "members/createMemberForm";
  }

  @PostMapping("/members/new")
  public String createMember(@Valid MemberForm form, BindingResult result) {

    if (result.hasErrors()) {
      return "members/createMemberForm";
    }

    Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
    Member member = new Member();
    member.setName(form.getName());
    member.setAddress(address);
    memberService.join(member);
    return "redirect:/";
  }

  @GetMapping("/members")
  public String list(Model model) {
    /*
      보통은 엔티티를 반환하지 않고, DTO 같은 객체를 만들어서 화면으로 전달해야함
      - API 경우, 엔티티 그대로 반환은 위험
      - Entity, 비즈니스 로직에 집중하도록 설계 해야 함. 화면 데이터와 분리 필요
     */
    final List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);
    return "members/memberList";
  }
}
