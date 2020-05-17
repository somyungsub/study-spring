package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

// 직접 구현 POJO 기반으로
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

  private final EntityManager em;

  public List<Member> findMemberCustom() {
    return em.createQuery("select m from Member m")
            .getResultList();
  }
}
