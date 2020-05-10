package io.ssosso.jpashop1.repository;

import io.ssosso.jpashop1.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class MemberRepository {

  @PersistenceContext
  private EntityManager em;

//  @PersistenceUnit  // factory 직접 주입받고 싶을 때
//  private EntityManagerFactory emf;

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
