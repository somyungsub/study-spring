package io.ssosso.springsecuritypractice1.sercurity.config;

import io.ssosso.springsecuritypractice1.sercurity.factory.MethodResourcesFactoryBean;
import io.ssosso.springsecuritypractice1.sercurity.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
// 내부적으로 isPrePostEnabled, isSecuredEnabled가 true가 되어야 활성화됨
// (GlobalMethodSecurityConfiguration -> methodSecurityMetadataSource bean 생성 부분 참조)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

  @Autowired
  private SecurityResourceService securityResourceService;

  @Override
  protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
//    super.customMethodSecurityMetadataSource(); // 기본 -> null 리턴(사용하지 않겠다는 의미)
    return mapBasedMethodSecurityMetadataSource();
  }

  @Bean
  public MethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
    // DB에서 얻은 map 전달
    return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
  }

  @Bean
  public MethodResourcesFactoryBean methodResourcesMapFactoryBean() {
    MethodResourcesFactoryBean methodResourcesFactoryBean = new MethodResourcesFactoryBean();
    methodResourcesFactoryBean.setSecurityResourceService(securityResourceService);
    return methodResourcesFactoryBean;
  }


}
