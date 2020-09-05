package com.thoughtmechanix.licenses.repository;

import com.thoughtmechanix.licenses.model.Organization;

/*
    레디스 통신에 사용될 Repo
 */
public interface OrganizationRedisRepository {
  void saveOrganization(Organization org);
  void updateOrganization(Organization org);
  void deleteOrganization(String organizationId);
  Organization findOrganization(String organizationId);
}
