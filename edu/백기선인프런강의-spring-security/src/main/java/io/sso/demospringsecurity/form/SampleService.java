package io.sso.demospringsecurity.form;


import io.sso.demospringsecurity.account.Account;
import io.sso.demospringsecurity.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {
  public void dashboard() {
//    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    final Object principal = authentication.getPrincipal();
//    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//    final Object credentials = authentication.getCredentials();
//    final boolean authenticated = authentication.isAuthenticated();

//    final Account account = AccountContext.getAccount();
//    System.out.println("====================");
//    System.out.println(account.getUsername());


    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final UserDetails principal = (UserDetails) authentication.getPrincipal();
    System.out.println("========================");
    System.out.println(authentication);
//    System.out.println(principal.getUsername());

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
