package io.sso.demospringsecurity.form;


import io.sso.demospringsecurity.account.Account;
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
}
