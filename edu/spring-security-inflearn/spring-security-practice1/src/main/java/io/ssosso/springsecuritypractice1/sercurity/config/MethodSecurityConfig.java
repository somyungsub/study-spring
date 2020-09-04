package io.ssosso.springsecuritypractice1.sercurity.config;

import io.ssosso.springsecuritypractice1.sercurity.factory.MethodResourcesMapFactoryBean;
import io.ssosso.springsecuritypractice1.sercurity.processor.ProtectPointcutPostProcessor;
import io.ssosso.springsecuritypractice1.sercurity.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

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
  public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
    // DB에서 얻은 map 전달
    return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
  }

  @Bean
  public MethodResourcesMapFactoryBean methodResourcesMapFactoryBean() {
    MethodResourcesMapFactoryBean methodResourcesMapFactoryBean = new MethodResourcesMapFactoryBean();
    methodResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
    methodResourcesMapFactoryBean.setResourceType("method");
    return methodResourcesMapFactoryBean;
  }

  @Bean
  public MethodResourcesMapFactoryBean pointcutResourcesMapFactoryBean() {
    MethodResourcesMapFactoryBean methodResourcesMapFactoryBean = new MethodResourcesMapFactoryBean();
    methodResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
    methodResourcesMapFactoryBean.setResourceType("pointcut");
    return methodResourcesMapFactoryBean;
  }

  @Bean
  public ProtectPointcutPostProcessor protectPointcutPostProcessor() {
    ProtectPointcutPostProcessor protectPointcutPostProcessor
      = new ProtectPointcutPostProcessor(mapBasedMethodSecurityMetadataSource());
    protectPointcutPostProcessor.setPointcutMap(pointcutResourcesMapFactoryBean().getObject());
    return protectPointcutPostProcessor;
  }

//  @Bean
//  @Profile("pointcut")
//  BeanPostProcessor protectPointcutPostProcessor() throws Exception {
//
//    Class<?> clazz = Class.forName("org.springframework.security.config.method.ProtectPointcutPostProcessor");
//    Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(MapBasedMethodSecurityMetadataSource.class);
//    declaredConstructor.setAccessible(true);
//    Object instance = declaredConstructor.newInstance(mapBasedMethodSecurityMetadataSource());
//    Method setPointcutMap = instance.getClass().getMethod("setPointcutMap", Map.class);
//    setPointcutMap.setAccessible(true);
//    setPointcutMap.invoke(instance, pointcutResourcesMapFactoryBean().getObject());
//
//    return (BeanPostProcessor) instance;
//  }


}