package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.entity.Member;
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

  @Test
  public void basicCRUD() {
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);


    // 단건조회  검증
    Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
    Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);


    // 리스트 조회 검증
    List<Member> all = memberJpaRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    // 카운트 검증
    long count = memberJpaRepository.count();
    assertThat(count).isEqualTo(2);


    // 삭제 검증
    memberJpaRepository.delete(member1);
    memberJpaRepository.delete(member2);

    long deleteCount = memberJpaRepository.count();
    assertThat(deleteCount).isEqualTo(0);

  }

  @Test
  public void findByUsernameAngAge() {
    createMember();

    List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
    assertThat(result.get(0).getUsername()).isEqualTo("AAA");
    assertThat(result.get(0).getAge()).isEqualTo(20);
  }


  @Test
  public void namedQuery() {
    createMember();
    List<Member> result = memberJpaRepository.findByUsername("AAA");
    System.out.println("result = " + result);
  }

  @Test
  public void paging() {

    // given
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member2", 10));
    memberJpaRepository.save(new Member("member3", 10));
    memberJpaRepository.save(new Member("member4", 10));
    memberJpaRepository.save(new Member("member5", 10));

    int age = 10;
    int offset = 0;
    int limit = 3;

    // when
    List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
    long totalCount = memberJpaRepository.totalCount(age);

    // then
    assertThat(members.size()).isEqualTo(3);
    assertThat(totalCount).isEqualTo(5);
  }

  @Test
  public void bulkUpdate() {

    // given
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member2", 19));
    memberJpaRepository.save(new Member("member3", 20));
    memberJpaRepository.save(new Member("member4", 21));
    memberJpaRepository.save(new Member("member5", 40));

    // when
    int result = memberJpaRepository.bulkAgePlus(20);

    // then
    assertThat(result).isEqualTo(3);

  }

  private void createMember() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("AAA", 20);

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);
  }

}