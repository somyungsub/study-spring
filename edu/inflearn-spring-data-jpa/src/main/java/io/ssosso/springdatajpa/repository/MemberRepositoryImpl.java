package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// 구현체 이름 규칙 : 레포지토리+Impl -> Spring이 인식해서 호출함
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  @PersistenceContext
  private final EntityManager em;

  @Override
  public List<Member> findMemberCustom() {
    return em.createQuery("select m from Member m")
            .getResultList();
  }

}
