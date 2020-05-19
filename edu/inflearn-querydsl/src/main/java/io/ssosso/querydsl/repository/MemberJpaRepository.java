package io.ssosso.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ssosso.querydsl.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static io.ssosso.querydsl.entity.QMember.member;

@Repository
public class MemberJpaRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

//  public MemberJpaRepository(EntityManager em) {
//    this.em = em;
//    this.queryFactory = new JPAQueryFactory(em);  // Spring bean으로 등록해서 사용해도 됨
//  }

  public MemberJpaRepository(EntityManager em, JPAQueryFactory queryFactory) {
    this.em = em;
    this.queryFactory = queryFactory;
  }

  public void save(Member member) {
    em.persist(member);
  }

  public Optional<Member> findById(Long id) {
    Member findMember = em.find(Member.class, id);
    return Optional.ofNullable(findMember);
  }

  public List<Member> findAll() {
    return em.createQuery("select m from Member m", Member.class).getResultList();
  }

  public List<Member> findAll_Querydsl() {
    return queryFactory
            .selectFrom(member)
            .fetch();
  }

  public List<Member> findByUsername(String username) {
    return em.createQuery("select m from Member m where m.username = :username")
            .setParameter("username", username)
            .getResultList();
  }

  public List<Member> findByUsername_Querydsl(String username) {
    return queryFactory
            .selectFrom(member)
            .where(member.username.eq(username))
            .fetch();
  }
}
