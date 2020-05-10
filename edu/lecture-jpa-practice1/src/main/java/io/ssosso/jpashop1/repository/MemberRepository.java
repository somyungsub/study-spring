package io.ssosso.jpashop1.repository;

import io.ssosso.jpashop1.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//  @PersistenceContext
//  @Autowired // Springboot 최신 가능
  private final EntityManager em;

//  @PersistenceUnit  // factory 직접 주입받고 싶을 때
//  private EntityManagerFactory emf;

//    @RequiredArgsConstructor 을 통해 Inject
//  public MemberRepository(EntityManager em) {
//    this.em = em;
//  }

  public void save(Member member) {
    em.persist(member);
  }

  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }

  public List<Member> findAll() {
    return em.createQuery("select m from Member m", Member.class)
            .getResultList();
  }

  public List<Member> findByName(String name) {
    return em.createQuery("select m from Member m where m.name=:name", Member.class)
            .setParameter("name", name)
            .getResultList();
  }
}
