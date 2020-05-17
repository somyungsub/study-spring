package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.entity.Member;
import io.ssosso.springdatajpa.entity.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

public class MemberSpec {

  public static Specification<Member> teamName(final String teamName) {
    return (root, query, builder) -> {

      if (StringUtils.isEmpty(teamName)) {
        return null;
      }

      Join<Member, Team> team = root.join("team", JoinType.INNER);// 회원과 조인
      return builder.equal(team.get("name"), teamName);
    };
  }

  public static Specification<Member> username(final String username) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
  }

}
