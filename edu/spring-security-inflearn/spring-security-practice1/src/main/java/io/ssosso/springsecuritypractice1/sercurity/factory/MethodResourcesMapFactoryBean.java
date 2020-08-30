package io.ssosso.springsecuritypractice1.sercurity.factory;

import io.ssosso.springsecuritypractice1.sercurity.service.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import java.util.LinkedHashMap;
import java.util.List;

public class MethodResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

  private SecurityResourceService securityResourceService;
  private String resourceType;
  private LinkedHashMap<String, List<ConfigAttribute>> resourceMap;

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public void setSecurityResourceService(SecurityResourceService securityResourceService) {
    this.securityResourceService = securityResourceService;
  }

  @Override
  public LinkedHashMap<String, List<ConfigAttribute>> getObject() {

    if ("method".equals(resourceType)) {
      resourceMap = securityResourceService.getMethodResourceList();
    } else if ("pointcut".equals(resourceType)) {
      resourceMap = securityResourceService.getPointcutResourceList();
    }
    return resourceMap;
  }

  @Override
  public Class<?> getObjectType() {
    return LinkedHashMap.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}
