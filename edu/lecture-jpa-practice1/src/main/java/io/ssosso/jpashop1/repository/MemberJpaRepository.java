package io.ssosso.jpashop1.repository;

import io.ssosso.jpashop1.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  // select m from Member m where m.name = ?
  List<Member> findByName(String name);
}
