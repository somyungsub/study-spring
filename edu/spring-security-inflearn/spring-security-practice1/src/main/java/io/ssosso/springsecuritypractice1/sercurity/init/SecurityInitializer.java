package io.ssosso.springsecuritypractice1.sercurity.init;


import io.ssosso.springsecuritypractice1.service.RoleHierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

/*
   스프링 컨텍스트 로딩시점 실행 됨 -> ApplicationRunner
 */
@Component
public class SecurityInitializer implements ApplicationRunner {

  @Autowired
  private RoleHierarchyService roleHierarchyService;

  @Autowired
  private RoleHierarchyImpl roleHierarchy;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    // DB -> 권한 계층 읽어서 roleHierarchy 에 저장하기
   String allHierarchy = roleHierarchyService.findAllHierarchy();
    roleHierarchy.setHierarchy(allHierarchy);
  }
}
