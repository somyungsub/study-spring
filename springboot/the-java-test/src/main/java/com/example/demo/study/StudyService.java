package com.example.demo.study;

import com.example.demo.domain.Member;
import com.example.demo.domain.Study;
import com.example.demo.member.MemberService;

import java.util.Optional;

public class StudyService {
  private final MemberService memberService;

  private final StudyRepository studyRepository;

  public StudyService(MemberService memberService, StudyRepository studyRepository) {
    assert memberService != null;
    assert studyRepository != null;

    this.memberService = memberService;
    this.studyRepository = studyRepository;
  }

  public Study createNewStudy(Long memberId, Study study) {
    Optional<Member> member = memberService.findById(memberId);
    study.setMember(member.orElseThrow(()-> new IllegalArgumentException("Member doesn't exist for id : " + memberId)));


    final Study save = studyRepository.save(study);

    memberService.notify(save);
    memberService.notify(member.get());

    return save;
  }

  public Study openStudy(Study study) {
    study.open();
    final Study save = studyRepository.save(study);
    memberService.notify(save);
    return save;
  }
}
