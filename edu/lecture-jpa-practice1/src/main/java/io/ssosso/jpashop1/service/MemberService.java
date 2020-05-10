package io.ssosso.jpashop1.service;

import io.ssosso.jpashop1.domain.Member;
import io.ssosso.jpashop1.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor     // injection 을 하기 위한 생성자 생성
@RequiredArgsConstructor  // final 필드만 생성자로 만들어 줌.. 좀더 안전한 방법
public class MemberService {

  private final MemberRepository memberRepository;

//  @Autowired  // 신규버전 -> 생성자가 1개 인 경우, 알아서 Spring이 Injection 해줌
//  public MemberService(MemberRepository memberRepository) {
//    this.memberRepository = memberRepository;
//  }

//  @Autowired  // 변경 가능성이 있으므로 생성자 인젝션이 안전함
//  public void setMemberRepository(MemberRepository memberRepository) {
//    this.memberRepository = memberRepository;
//  }



  // 1.회원 가입
  @Transactional  // Type 보다 우선권
  public Long join(Member member) {
    validateDuplicateMember(member); // 중복회원 검증
    memberRepository.save(member);
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    // exception
    List<Member> findMembers = memberRepository.findByName(member.getName());
    if (findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  // 2.회원 전체 조회
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  public Member findOne(Long memberId) {
    return memberRepository.findOne(memberId);
  }

}
