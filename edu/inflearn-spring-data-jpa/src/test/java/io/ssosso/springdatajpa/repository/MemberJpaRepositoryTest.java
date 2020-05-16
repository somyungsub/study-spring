package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

  @Autowired
  MemberJpaRepository memberJpaRepository;

  @Test
  public void testMember() {
    Member member = new Member("memberA");
    Member saveMember = memberJpaRepository.save(member);

    final Member findMember = memberJpaRepository.find(saveMember.getId());

    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    assertThat(findMember).isEqualTo(member);

    // 같은 영속성 컨텍스트 -> 같은 인스턴스, 동일성 보장, 1차캐시
    System.out.println("findMember = " + findMember);
    System.out.println("member = " + member);
  }


}