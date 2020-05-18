package io.ssosso.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ssosso.querydsl.entity.Member;
import io.ssosso.querydsl.entity.QMember;
import io.ssosso.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static io.ssosso.querydsl.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;

  @BeforeEach
  public void setUp() {
    queryFactory = new JPAQueryFactory(em);

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
  @DisplayName("JPQL 기본사용")
  public void start_jqpl() {
    // member1을 찾아라.
    String sql = "select m from Member m where m.username = :username";
    Member findMember = em.createQuery(sql, Member.class)
            .setParameter("username", "member1")
            .getSingleResult();

    assertThat(findMember.getUsername()).isEqualTo("member1");
  }

  @Test
  @DisplayName("기본사용법")
  public void start_querydsl() {

    QMember m = new QMember("m"); // 구분자 이름 주는 것임.

    Member findMember = queryFactory
            .select(m)
            .from(m)
            .where(m.username.eq("member1"))
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");

  }

  @Test
  @DisplayName("q-type import static")
  public void start_querydsl_qtype() {

    Member findMember = queryFactory
            .select(member)
            .from(member)
            .where(member.username.eq("member1"))
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");

  }

  @Test
  @DisplayName("q-type 별칭 ")
  public void start_querydsl_qtype2() {
    QMember m1 = new QMember("m1");
    Member findMember = queryFactory
            .select(m1)
            .from(m1)
            .where(m1.username.eq("member1"))
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");

  }

  @Test
  @DisplayName("검색조회")
  public void search() {
    Member findMember = queryFactory
            .selectFrom(member)
            .where(member.username.eq("member1")
                    .and(member.age.eq(10)))
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
  }

  @Test
  @DisplayName("검색조회 - 파라미터 and")
  public void search_and_param() {
    Member findMember = queryFactory
            .selectFrom(member)
            .where(
                    member.username.eq("member1"),  // and
                    member.age.eq(10)
            )
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
  }

  @Test
  @DisplayName("결과조회")
  public void resultFetch() {

    // 다건
    List<Member> fetch = queryFactory
            .selectFrom(member)
            .fetch();

    // 단건 2개이상 예외발생
    Member fetchOne = queryFactory
            .selectFrom(QMember.member)
            .fetchOne();

    // 발견 첫째
    Member fetchFirst = queryFactory
            .selectFrom(QMember.member)
            .fetchFirst();

    // 결과 -> Page 정보 동반
    QueryResults<Member> results = queryFactory
            .selectFrom(QMember.member)
            .fetchResults();

    // count 조회 쿼리 발생
    results.getTotal();
    List<Member> content = results.getResults();

    // count 조회 쿼리
    long total = queryFactory
            .selectFrom(member)
            .fetchCount();
  }



}
