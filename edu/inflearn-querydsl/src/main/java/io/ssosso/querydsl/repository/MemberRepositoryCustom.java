package io.ssosso.querydsl.repository;

import io.ssosso.querydsl.dto.MemberSearchCondition;
import io.ssosso.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
  List<MemberTeamDto> search(MemberSearchCondition condition);
}
