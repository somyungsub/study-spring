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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

  @Autowired
  EntityManager em;

  @Autowired
  MemberRepository memberRepository;

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
  @DisplayName("스프링데이터JPA 기본 - Repo")
  public void spring_data_jpa_repo_basic() {
    Member member = new Member("member1", 10);
    memberRepository.save(member);

    Member findMember = memberRepository.findById(member.getId()).get();
    assertThat(findMember).isEqualTo(member);

    List<Member> result1 = memberRepository.findAll();
    assertThat(result1).containsExactly(member);

    List<Member> result2 = memberRepository.findByUsername("member1");
    assertThat(result2).containsExactly(member);
  }

  @Test
  @DisplayName("조건검색 - 사용자정의 인터페이스")
  public void search_user_custom() {
    MemberSearchCondition condition = new MemberSearchCondition();
    condition.setAgeGoe(35);
    condition.setAgeLoe(40);
    condition.setTeamName("teamB");

    List<MemberTeamDto> result = memberRepository.search(condition);
    assertThat(result).extracting("username").containsExactly("member4");
    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("조건검색 - POJO 직접구현")
  public void search_pojo() {
    MemberSearchCondition condition = new MemberSearchCondition();
    condition.setAgeGoe(35);
    condition.setAgeLoe(40);
    condition.setTeamName("teamB");

    MemberQueryRepository memberQueryRepository = new MemberQueryRepository(em);
    List<MemberTeamDto> result = memberQueryRepository.search(condition);
    assertThat(result).extracting("username").containsExactly("member4");
    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("search 페이지 심플")
  public void search_page_simple() {
    MemberSearchCondition condition = new MemberSearchCondition();
    PageRequest pageRequest = PageRequest.of(0, 3);
    Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageRequest);

    assertThat(result.getSize()).isEqualTo(3);
    assertThat(result.getContent()).extracting("username").containsExactly("member1","member2","member3");


  }


}