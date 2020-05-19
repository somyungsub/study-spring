package io.ssosso.querydsl.repository;

import io.ssosso.querydsl.dto.MemberSearchCondition;
import io.ssosso.querydsl.dto.MemberTeamDto;
import io.ssosso.querydsl.entity.Member;
import io.ssosso.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

  @Autowired
  EntityManager em;

  @Autowired
  MemberJpaRepository memberJpaRepository;

  @BeforeEach
  public void setUp() {

    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamA);
    Member member3 = new Member("member3", 30, teamB);
    Member member4 = new Member("member4", 40, teamB);

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);

  }

  @Test
  @DisplayName("순수 JPA, JPQL 사용")
  public void basic_jpql() {
    Member member = new Member("member1",10);
    memberJpaRepository.save(member);

    Member findMember = memberJpaRepository.findById(member.getId()).get();
    assertThat(findMember).isEqualTo(member);

    List<Member> result1 = memberJpaRepository.findAll();
    assertThat(result1).containsExactly(member);

    List<Member> result2 = memberJpaRepository.findByUsername("member1");
    assertThat(result2).containsExactly(member);
  }

  @Test
  @DisplayName("Quertdsl로 변경한 테스트")
  public void basic_querydsl() {
    Member member = new Member("member1",10);
    memberJpaRepository.save(member);

    Member findMember = memberJpaRepository.findById(member.getId()).get();
    assertThat(findMember).isEqualTo(member);

    List<Member> result1 = memberJpaRepository.findAll_Querydsl();
    assertThat(result1).containsExactly(member);

    List<Member> result2 = memberJpaRepository.findByUsername_Querydsl("member1");
    assertThat(result2).containsExactly(member);

  }

  @Test
  @DisplayName("조건거색1")
  public void search_builder() {
    MemberSearchCondition condition = new MemberSearchCondition();
    condition.setAgeGoe(35);
    condition.setAgeLoe(40);
    condition.setTeamName("teamB");

    List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
    assertThat(result).extracting("username").containsExactly("member4");
    result.forEach(o -> System.out.println("o = " + o));
  }
  @Test
  @DisplayName("조건검색2")
  public void search_builder2() {
    MemberSearchCondition condition = new MemberSearchCondition();
    condition.setTeamName("teamB");

    List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
    assertThat(result).extracting("username").containsExactly("member3","member4");
    result.forEach(o -> System.out.println("o = " + o));
  }
  @Test
  @DisplayName("조건 없는 경우")
  public void search_builder3() {
    MemberSearchCondition condition = new MemberSearchCondition();

    // where 적용 x -> 문제없이 실행,but 조인된 모든데이터를 들고오므로, 주의 필요
    List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
    result.forEach(o -> System.out.println("o = " + o));
  }

}