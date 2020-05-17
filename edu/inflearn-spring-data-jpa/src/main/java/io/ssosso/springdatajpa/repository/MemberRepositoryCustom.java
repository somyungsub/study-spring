package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
  List<Member> findMemberCustom();

}
