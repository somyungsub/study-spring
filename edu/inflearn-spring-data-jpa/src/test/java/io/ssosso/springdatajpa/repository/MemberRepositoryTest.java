package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.dto.MemberDto;
import io.ssosso.springdatajpa.entity.Member;
import io.ssosso.springdatajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  TeamRepository teamRepository;

  @Test
  public void test() {
    // proxy
    System.out.println("memberRepository = " + memberRepository.getClass());
  }

  @Test
  public void testMember() {
    Member member = new Member("memberJPA A");
    Member save = memberRepository.save(member);

    Member findMember = memberRepository.findById(save.getId()).get();

    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

  }

  @Test
  public void basicCRUD() {
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    memberRepository.save(member1);
    memberRepository.save(member2);


    // 단건조회  검증
    Member findMember1 = memberRepository.findById(member1.getId()).get();
    Member findMember2 = memberRepository.findById(member2.getId()).get();

    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);


    // 리스트 조회 검증
    List<Member> all = memberRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    // 카운트 검증
    long count = memberRepository.count();
    assertThat(count).isEqualTo(2);


    // 삭제 검증
    memberRepository.delete(member1);
    memberRepository.delete(member2);

    long deleteCount = memberRepository.count();
    assertThat(deleteCount).isEqualTo(0);

  }

  @Test
  public void findByUsernameAngAge() {
    createMember();

    List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
    assertThat(result.get(0).getUsername()).isEqualTo("AAA");
    assertThat(result.get(0).getAge()).isEqualTo(20);
  }

  @Test
  public void top3() {
    createMember();
    List<Member> top3 = memberRepository.findTop3MemberBy();
    System.out.println("top3 = " + top3);
  }

  @Test
  public void namedQuery() {
    createMember();
    List<Member> result = memberRepository.findByUsername("AAA");
    System.out.println("result = " + result);
  }

  @Test
  public void query_repo() {
    createMember();
    List<Member> result = memberRepository.findUser("AAA", 10);
    System.out.println("result = " + result);
  }

  @Test
  public void find_username_list() {
    createMember();
    List<String> usernameList = memberRepository.findUsernameList();
    usernameList.forEach(System.out::println);
  }

  @Test
  public void find_memberdto() {
    Team team = new Team("teamA");
    teamRepository.save(team);


    Member m1 = new Member("AAA", 10);
    m1.setTeam(team);
    memberRepository.save(m1);


    List<MemberDto> memberDto = memberRepository.findMemberDto();
    memberDto.forEach(System.out::println);
  }

  private void createMember() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("AAA", 20);
    Member member3 = new Member("CCC", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);
  }

}