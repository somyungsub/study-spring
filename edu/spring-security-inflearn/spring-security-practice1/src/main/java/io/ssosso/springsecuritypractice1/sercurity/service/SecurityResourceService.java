package io.ssosso.springsecuritypractice1.sercurity.service;

import io.ssosso.springsecuritypractice1.domain.entity.Resources;
import io.ssosso.springsecuritypractice1.repository.ResourcesRepository;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SecurityResourceService {

  private ResourcesRepository resourcesRepository;

  public SecurityResourceService(ResourcesRepository resourcesRepository) {
    this.resourcesRepository = resourcesRepository;
  }

  public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
    LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
    List<Resources> resourcesList = resourcesRepository.findAllResources();

    resourcesList
      .forEach(resources -> {
        List<ConfigAttribute> configAttributes = new ArrayList<>();
        resources.getRoleSet().forEach(role -> {
          configAttributes.add(new SecurityConfig(role.getRoleName()));
          result.put(new AntPathRequestMatcher(resources.getResourceName()), configAttributes);
        });
      });

    return result;
  }

  public List<String> getAccessIpList() {
    return null;
  }
}
