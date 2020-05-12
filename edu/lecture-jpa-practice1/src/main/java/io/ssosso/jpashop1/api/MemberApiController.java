package io.ssosso.jpashop1.api;

import io.ssosso.jpashop1.domain.Member;
import io.ssosso.jpashop1.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController// @Controller + @ResponseBody
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @GetMapping("/api/v1/members")
  public List<Member> memberV1() {
    return memberService.findMembers();
  }

  @GetMapping("/api/v2/members")
  public Result memberV2() {

    // 한번 래핕 작업을 권장 -> api 스펙 공개후, 변경에 유연
    final List<Member> findMembers = memberService.findMembers();
    final List<MemberDto> collect = findMembers.stream()
            .map(member -> new MemberDto(member.getName()))
            .collect(Collectors.toList());

    return new Result(collect);
  }
  @PostMapping("/api/v1/members")
  public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {

    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  /**
   * 도메인과 In/Out을 분리하여 사용해야함.
   * - 변경사항 or 노출의 위험도에 대비
   */
  @PostMapping("/api/v2/members")
  public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

    Member member = new Member();
    member.setName(request.getName());

    Long id = memberService.join(member);

    return new CreateMemberResponse(id);
  }

  @PutMapping("/api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(@PathVariable Long id,
                                             @RequestBody @Valid UpdateMemberRequest request) {
    // update -> 가급적 변경감지 활용
    memberService.update(id, request.getName());
    Member findMember = memberService.findOne(id);

    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
  }

  @Data
  static class CreateMemberResponse {
    private Long id;

    public CreateMemberResponse(Long id) {
      this.id = id;
    }
  }

  @Data
  static class CreateMemberRequest {
    private String name;
  }

  @Data
  @AllArgsConstructor
  static private class UpdateMemberResponse {
    private Long id;
    private String name;
  }

  @Data
  static private class UpdateMemberRequest {
    private String name;
  }

  @Data
  @AllArgsConstructor
  static private class Result<T> {
    private T data;
  }

  @Data
  @AllArgsConstructor
  static private class MemberDto {
    private String name;
  }
}
