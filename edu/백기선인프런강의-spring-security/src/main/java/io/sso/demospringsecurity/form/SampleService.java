package io.sso.demospringsecurity.form;


import io.sso.demospringsecurity.account.Account;
import io.sso.demospringsecurity.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;

@Service
public class SampleService {
  // 메서드 호출전 권한 검사
//  @Secured("ROLE_USER")
  @Secured({"ROLE_USER", "ROLE_ADMIN"})
//  @RolesAllowed("ROLE_USER")
//  @PreAuthorize("hasRole('USER')")
//  @PreAuthorize("#name == authentication.principal.username")
//  @PostAuthorize("returnObject.username == authentication.principal.nickName")   // 인가후 작업 가능하게
  public void dashboard() { // String name parameter
//    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    final Object principal = authentication.getPrincipal();
//    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//    final Object credentials = authentication.getCredentials();
//    final boolean authenticated = authentication.isAuthenticated();

//    final Account account = AccountContext.getAccount();
//    System.out.println("====================");
//    System.out.println(account.getUsername());

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    System.out.println("========================");
    System.out.println(authentication);
    System.out.println(userDetails.getUsername());

  }

  /*
    별도의 쓰레드를 만들어서 비동기적으로 호출을 해줌
    애노테이션을 붙이다고해서 비동기로 처리 되지는 않음 -> 쓰레드가 전부 동일함
    에러나는데, 시큐리티 컨텍스트가 공유가 안되는 점 유의 -> 해결해야됨
   */
  @Async
  public void asyncService() {
    SecurityLogger.log("Async Service");
    System.out.println("Async service is called.");
  }
}


