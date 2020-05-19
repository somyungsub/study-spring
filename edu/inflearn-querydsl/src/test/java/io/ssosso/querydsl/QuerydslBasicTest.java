package io.ssosso.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ssosso.querydsl.dto.MemberDto;
import io.ssosso.querydsl.dto.QMemberDto;
import io.ssosso.querydsl.dto.UserDto;
import io.ssosso.querydsl.entity.Member;
import io.ssosso.querydsl.entity.QMember;
import io.ssosso.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static io.ssosso.querydsl.entity.QMember.member;
import static io.ssosso.querydsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
  @Autowired
  EntityManager em;

  // QueryDsl 필수 사용 객체
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

  /**
   * 팀A 소속된 모든 회원 조인
   */
  @Test
  @DisplayName("조인")
  public void join() {
    List<Member> result = queryFactory
            .selectFrom(member)
            .leftJoin(member.team, team)
//            .join(member.team, team)
            .where(team.name.eq("teamA"))
            .fetch();

    assertThat(result)
            .extracting("username")
            .containsExactly("member1", "member2");
  }

  /**
   * 세타조인
   * 회원의 이름이 팀 이름과 같은 회원 조회
   */
  @Test
  @DisplayName("세타조인")
  public void theta_join() {
    em.persist(new Member("teamA"));
    em.persist(new Member("teamB"));
    em.persist(new Member("teamC"));


    List<Member> result = queryFactory
            .select(member)
            .from(member, team)
            .where(member.username.eq(team.name))
            .fetch();

    assertThat(result)
            .extracting("username")
            .containsExactly("teamA", "teamB");

  }

  /**
   * 예) 회원과 팀을 조인하면서, 팀이름이 teamA인 팀만 조인, 회원은 모두 조회
   * JPQL : select m, t from Member m left join m.team t on team.name = 'teamA'
   */
  @Test
  @DisplayName("조인 on")
  public void join_on_filtering() {
    em.persist(new Member("teamA"));
    em.persist(new Member("teamB"));
    em.persist(new Member("teamC"));

    List<Tuple> result = queryFactory
            .select(member, team)
            .from(member)
//            .leftJoin(member.team, team)
            .join(member.team, team)
            .on(team.name.eq("teamA"))
            .fetch();

    result.forEach(System.out::println);
  }

  /**
   * 연관관계 없는 엔티티 외부 조인
   * 회원의 이름이 팀 이름과 같은 대상 외부 조인
   */
  @Test
  @DisplayName("조인 on - 연관관계 없는 엔티티 외부조인")
  public void join_on_no_relation() {

    em.persist(new Member("teamA"));
    em.persist(new Member("teamB"));
    em.persist(new Member("teamC"));

    List<Tuple> result = queryFactory
            .select(member, team)
            .from(member)
//            .join(team) // 막조인, 연관관계가 없는 member, team 이라고 가정될 때
            .leftJoin(team) // 막조인, 연관관계가 없는 member, team 이라고 가정될 때
            .on(member.username.eq(team.name))
            .fetch();

    result.forEach(System.out::println);
  }

  @PersistenceUnit
  EntityManagerFactory emf;

  @Test
  @DisplayName("페치조인 미적용")
  public void fetch_join_no() {
    em.flush();
    em.clear();

    Member findMemer = queryFactory
            .selectFrom(member)
            .where(member.username.eq("member1"))
            .fetchOne();
    System.out.println("findMemer = " + findMemer);

    boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMemer.getTeam());
    assertThat(loaded).as("페치조인 미적용").isFalse();
  }

  @Test
  @DisplayName("페치조인 적용")
  public void fetch_join() {
    em.flush();
    em.clear();

    Member findMemer = queryFactory
            .selectFrom(member)
            .join(member.team, team)
            .fetchJoin()  // 페치 조인
            .where(member.username.eq("member1"))
            .fetchOne();
    System.out.println("findMemer = " + findMemer);

    boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMemer.getTeam());
    assertThat(loaded).as("페치조인 적용").isTrue();
  }

  /**
   * 나이가 가장 많은 회원 조회
   */
  @Test
  @DisplayName("서브쿼리")
  public void sub_query() {
    QMember memberSub = new QMember("memberSub");
    List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(
                    select(memberSub.age.max())
                            .from(memberSub)
            ))
            .fetch();

      assertThat(result)
              .extracting("age")  // age 필드
              .containsExactly(40);// 기대값

  }

  /**
   * 나이가 평균 이상인 회원
   */
  @Test
  @DisplayName("서브쿼리 goe")
  public void sub_query_goe() {
    QMember memberSub = new QMember("memberSub");
    List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.goe(
                    select(memberSub.age.avg())
                            .from(memberSub)
            ))
            .fetch();

      assertThat(result)
              .extracting("age")  // age 필드
              .containsExactly(30,40);// 기대값
  }

  /**
   *
   */
  @Test
  @DisplayName("서브쿼리 in")
  public void sub_query_in() {
    QMember memberSub = new QMember("memberSub");
    List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.in(
                    select(memberSub.age)
                            .from(memberSub)
                            .where(memberSub.age.gt(10))
            ))
            .fetch();

      assertThat(result)
              .extracting("age")  // age 필드
              .containsExactly(20, 30, 40);// 기대값
  }

  @Test
  @DisplayName("서브쿼리 select 절")
  public void sub_query_select() {
    QMember memberSub = new QMember("memberSub");
    List<Tuple> result = queryFactory
            .select(member.username,
                    select(memberSub.age.avg())
                            .from(memberSub))
            .from(member)
            .fetch();

    result.forEach(System.out::println);
  }

  @Test
  @DisplayName("case 문")
  public void case_basic() {
    List<String> result = queryFactory
            .select(member.age
                    .when(10).then("열살")
                    .when(20).then("스무살")
                    .otherwise("기타")
            )
            .from(member)
            .fetch();

    result.forEach(System.out::println);
  }

  @Test
  @DisplayName("case 복작")
  public void case_complex() {
    List<String> result = queryFactory
            .select(new CaseBuilder()
                    .when(member.age.between(0, 20)).then("0~20살")
                    .when(member.age.between(21, 30)).then("21~30살")
                    .otherwise("기타"))
            .from(member)
            .fetch();

    result.forEach(System.out::println);
  }

  @Test
  @DisplayName("상수")
  public void constant() {
    List<Tuple> result = queryFactory
            .select(member.username, Expressions.constant("A"))
            .from(member)
            .fetch();
    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("문자더하기")
  public void concat() {

    // {username}_{age}
    List<String> result = queryFactory
            .select(member.username.concat("_").concat(member.age.stringValue())) // stringValue 잘 쓰임,
            .from(member)
            .where(member.username.eq("member1"))
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("프로젝션-1개인경우")
  public void projection_one() {
    List<String> result = queryFactory
            .select(member.username)
            .from(member)
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("프로젝션-2개이상")
  public void projection_tuple() {
    List<Tuple> result = queryFactory
            .select(member.username, member.age)
            .from(member)
            .fetch();

    result.forEach(tuple -> {
      System.out.println("tuple.get(member.username) = " + tuple.get(member.username));
      System.out.println("tuple.get(member.age) = " + tuple.get(member.age));
    });
  }

  @Test
  @DisplayName("DTO 조회 - JPQL")
  public void find_dto_by_jpql() {
    List<MemberDto> result = em.createQuery(
            "select new io.ssosso.querydsl.dto.MemberDto(m.username, m.age) " +
                    "from Member m", MemberDto.class)
            .getResultList();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("DTO 조회 - QueryDsl [setter]")
  public void find_dto_by_querydsl_setter() {
    List<MemberDto> result = queryFactory
            .select(Projections.bean(
                    MemberDto.class,
                    member.username,
                    member.age))
            .from(member)
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("DTO 조회 - QueryDsl [field]")
  public void find_dto_by_querydsl_field() {
    List<MemberDto> result = queryFactory
            .select(Projections.fields(
                    MemberDto.class,
                    member.username,
                    member.age))
            .from(member)
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("DTO 조회 - QueryDsl [field-별칭]")
  public void find_dto_by_querydsl_field_alias() {
    List<UserDto> result = queryFactory
            .select(Projections.fields(
                    UserDto.class,
                    member.username.as("name"), // field명이 다른 경우 세팅법
                    member.age))
            .from(member)
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("DTO 조회 - QueryDsl [field-별칭]")
  public void find_dto_by_querydsl_field_alias2() {
    // 예제를 위한 예제
    QMember memberSub = new QMember("memberSub");
    List<UserDto> result = queryFactory
            .select(Projections.fields(
                    UserDto.class,
                    member.username.as("name"),
                    ExpressionUtils.as(JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub), "age"))
            )
            .from(member)
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("DTO 조회 - QueryDsl [constructor]")
  public void find_dto_by_querydsl_constructor() {
    List<MemberDto> result = queryFactory
            .select(Projections.constructor(
                    MemberDto.class,
                    member.username,
                    member.age))
            .from(member)
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("query_projection")
  public void query_projection() {
    List<MemberDto> result = queryFactory
            .select(new QMemberDto(member.username, member.age))
            .from(member)
            .fetch();

    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("동적쿼리-boolean_builder")
  public void boolean_builder() {
    String usernameParam = "member1";
    Integer ageParam = null;

    List<Member> result = searchMember1(usernameParam, ageParam);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("동적쿼리-where 다중 파라미터 ")
  public void where_multi_parameter() {
    String usernameParam = "member1";
    Integer ageParam = null;

    List<Member> result = searchMember2(usernameParam, ageParam);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("수정 삭제 배치")
  @Commit
  public void bulk_update() {
    // member1 = 10 -> DB member1
    // member2 = 20 -> DB member2
    // member3 = 30 -> DB member3
    // member4 = 40 -> DB member4

    // DB 반영, but 영속성컨텍스트는 1차캐시로 존재
    long count = queryFactory
            .update(member)
            .set(member.username, "비회원")
            .where(member.age.lt(28))
            .execute();


    // member1 = 10 -> DB 비회원
    // member2 = 20 -> DB 비회원
    // member3 = 30 -> DB member3
    // member4 = 40 -> DB member4
    /*
      주의!!
        영속성컨텍스트 -> member1~4가 존재 -> DB에서 가져온 변경된 데이터 들고와도.. 컨텍스트에 존재하면, DB 데이터를 버리고 1차캐시를 사용
      해결
        영속성컨텍스트 초기화 -> em.flush(); em.clear();
        컨텍스트를 비우고 DB 조회해야.. DB에 반영된 데이터를 들고 옴!!
     */

    em.flush(); em.clear();

    List<Member> list = queryFactory
            .selectFrom(member)
            .fetch();
    list.forEach(o -> System.out.println("o = " + o));

  }

  @Test
  @DisplayName("벌크 수정")
  @Commit
  public void bulk_add() {
    // 쿼리 확인
    long count = queryFactory
            .update(member)
            .set(member.age, member.age.add(1))
//            .set(member.age, member.age.subtract(2))
//            .set(member.age, member.age.multiply(2))
//            .set(member.age, member.age.divide(2))
            .execute();

  }

  @Test
  @DisplayName("벌크 삭제")
  public void bulk_delete() {
    // 쿼리 확인
    long count = queryFactory
            .delete(member)
            .where(member.age.gt(18))
            .execute();
  }

  @Test
  @DisplayName("sql 함수 사용")
  public void sql_function() {

    List<String> result = queryFactory
//            .select(Expressions.stringTemplate(
//                    "function('replace', {0}, {1}, {2})",
//                    member.username, "M"))
            .select(Expressions.stringTemplate(
                    "function('upper', {0})",  // function 정의, 등록된 것만 Dialect, DB
                    member.username))           // index 파람 순서
            .from(member)
            .fetch();
    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("sql 함수 소문자")
  public void sql_function2() {
    List<String> result = queryFactory
            .select(member.username)
            .from(member)
            .where(member.username.eq(
                    Expressions.stringTemplate(
                            "function('lower', {0})",
                            member.username)
                    )
            )
            .fetch();
    result.forEach(o -> System.out.println("o = " + o));
  }

  @Test
  @DisplayName("sql 함수 - 내장함수 (안시표준)")
  public void sql_function3() {
    List<String> result = queryFactory
            .select(member.username)
            .from(member)
            .where(member.username.eq(member.username.lower()))
//            .where(member.username.eq(member.username.upper()))
            .fetch();
    result.forEach(o -> System.out.println("o = " + o));
  }

  private List<Member> searchMember2(String usernameParam, Integer ageParam) {
    return queryFactory
            .selectFrom(member)
            .where(allEq(usernameParam, ageParam))
//            .where(usernameEq(usernameParam), ageEq(ageParam))
            .fetch();
  }


  /*
      조립이 가능하다... 재사용 극대화 가능
      - 광고상태 isValid , 날짜 IN is~  등등...
   */
  private BooleanExpression allEq(String usernameParam, Integer ageParam) {
    return usernameEq(usernameParam).and(ageEq(ageParam));
  }

  private BooleanExpression ageEq(Integer ageParam) {
    return ageParam == null ? null : member.age.eq(ageParam);
  }

  private BooleanExpression usernameEq(String usernameParam) {
    return usernameParam == null ? null : member.username.eq(usernameParam);
  }

  private List<Member> searchMember1(String usernameParam, Integer ageParam) {
    BooleanBuilder builder = new BooleanBuilder();

    if (usernameParam != null) {
      builder.and(member.username.eq(usernameParam));
    }

    if (ageParam != null) {
      builder.and(member.age.eq(ageParam));
    }

    List<Member> list = queryFactory
            .selectFrom(member)
            .where(builder)
            .fetch();
    return list;
  }

}
