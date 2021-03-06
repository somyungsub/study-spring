package io.ssosso.jpashop1;

import io.ssosso.jpashop1.domain.Member;
import io.ssosso.jpashop1.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName("memberTest")
  @Transactional    // test -> 롤백
  @Rollback(false)  // rollback 안함
  public void test() {
    // given
    Member member = new Member();
    member.setName("memberA");

    // when
    memberRepository.save(member);
    final Member findMember = memberRepository.findOne(member.getId());

    // then
    Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
    Assertions.assertThat(findMember).isEqualTo(member);

    // 같은 영속성 컨텍스트 -> 1차캐시, 같은 객체  / select 문 실행 안됨 find 에서
    System.out.println("(findMember==member) = " + (findMember == member));

  }

}