package io.ssosso.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ssosso.querydsl.entity.Member;
import io.ssosso.querydsl.entity.QMember;
import io.ssosso.querydsl.entity.QTeam;
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
import static io.ssosso.querydsl.entity.QTeam.*;
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

  /**
   * 회원 정렬 순서
   * 1. 회원 나이 내림차순 (desc)
   * 2. 회원 이름 오름차순 (asc)
   *  단, 2에서 회원 이름이 없으면 -> 마지막에 출력 (nulls last)
   */
  @Test
  @DisplayName("정렬")
  public void sort() {

    em.persist(new Member(null, 100));
    em.persist(new Member("member5", 100));
    em.persist(new Member("member6", 100));

    List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(100))
            .orderBy(member.age.desc(), member.username.asc().nullsLast())
            .fetch();

    Member member5 = result.get(0);
    Member member6 = result.get(1);
    Member memberNull = result.get(2);

    assertThat(member5.getUsername()).isEqualTo("member5");
    assertThat(member6.getUsername()).isEqualTo("member6");
    assertThat(memberNull.getUsername()).isNull();
  }

  @Test
  @DisplayName("페이징")
  public void paging() {
    List<Member> result = queryFactory
            .selectFrom(member)
            .orderBy(member.username.desc())
            .offset(1)
            .limit(2)
            .fetch();

    assertThat(result.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("페이징")
  public void paging2() {

    QueryResults<Member> results = queryFactory
            .selectFrom(member)
            .orderBy(member.username.desc())
            .offset(1)
            .limit(2)
            .fetchResults();

    assertThat(results.getTotal()).isEqualTo(4);
    assertThat(results.getLimit()).isEqualTo(2);
    assertThat(results.getOffset()).isEqualTo(1);
    assertThat(results.getResults().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("집합")
  public void aggregation() {
    List<Tuple> result = queryFactory
            .select(
                    member.count(),
                    member.age.sum(),
                    member.age.avg(),
                    member.age.max(),
                    member.age.min()
            )
            .from(member)
            .fetch();

    Tuple tuple = result.get(0);
    assertThat(tuple.get(member.count())).isEqualTo(4);
    assertThat(tuple.get(member.age.sum())).isEqualTo(100);
    assertThat(tuple.get(member.age.avg())).isEqualTo(25);
    assertThat(tuple.get(member.age.max())).isEqualTo(40);
    assertThat(tuple.get(member.age.min())).isEqualTo(10);
  }


  /**
   * 팀의 이름과 각팀의 평균 연령을 구해라.
   */
  @Test
  @DisplayName("그룹바이")
  public void group_by() throws Exception {
    // given
    List<Tuple> result = queryFactory
            .select(team.name, member.age.avg())
            .from(member)
            .join(member.team, team)
            .groupBy(team.name)
            .fetch();

    Tuple teamA = result.get(0);
    Tuple teamB = result.get(1);

    assertThat(teamA.get(team.name)).isEqualTo("teamA");
    assertThat(teamA.get(member.age.avg())).isEqualTo(15);  // (10+20) / 2

    assertThat(teamB.get(team.name)).isEqualTo("teamB");
    assertThat(teamB.get(member.age.avg())).isEqualTo(35);  // (30+40) / 2

  }

}
