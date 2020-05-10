package io.ssosso.jpashop1.service;

import io.ssosso.jpashop1.domain.Member;
import io.ssosso.jpashop1.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // rollback
class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager em;

  @Test
//  @Rollback(false)  // rollback 안하고 commit 함 -> insert 문 실행됨 (JPA-플러쉬)
  public void 회원가입() throws Exception {
    // given
    Member member = new Member();
    member.setName("So");

    // when
    final Long saveId = memberService.join(member);

    // then
    em.flush(); // 쿼리 실행 -> 그러나 @Transactional에 의해 롤백 됨
    assertEquals(member, memberRepository.findOne(saveId));

  }

  @Test
  public void 중복_회원_예외() throws Exception {
    // given
    Member member = new Member();
    member.setName("so1");

    Member member2 = new Member();
    member2.setName("so1");

    // when
    memberService.join(member);

    // then : 예외가 발생해야 한다 !
    assertThrows(IllegalStateException.class, () -> {
      memberService.join(member2);
    });

  }

}