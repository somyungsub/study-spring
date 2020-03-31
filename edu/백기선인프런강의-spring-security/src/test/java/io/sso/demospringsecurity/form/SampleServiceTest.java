package io.sso.demospringsecurity.form;

import io.sso.demospringsecurity.account.Account;
import io.sso.demospringsecurity.account.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleServiceTest {

  @Autowired
  SampleService sampleService;

  @Autowired
  AccountService accountService;

  @Autowired
  AuthenticationManager authenticationManager;

  @Test
  @WithMockUser
  public void dashboard() {
    // principal
    Account account = new Account();
    account.setRole("USER");
    account.setUsername("keesun");
    account.setPassword("123");
    accountService.createNew(account);
    final UserDetails userDetails = accountService.loadUserByUsername("keesun");

    UsernamePasswordAuthenticationToken token
            = new UsernamePasswordAuthenticationToken(userDetails, "123");

    final Authentication authenticate = authenticationManager.authenticate(token);

    SecurityContextHolder.getContext().setAuthentication(authenticate);

    sampleService.dashboard();
  }
  @Test
  public void dashboard_admin() {
    // principal
    Account account = new Account();
    account.setRole("ADMIN"); // 하이라키를 인식 못하는거 확인
    account.setUsername("keesun");
    account.setPassword("123");
    accountService.createNew(account);
    final UserDetails userDetails = accountService.loadUserByUsername("keesun");

    UsernamePasswordAuthenticationToken token
            = new UsernamePasswordAuthenticationToken(userDetails, "123");

    final Authentication authenticate = authenticationManager.authenticate(token);

    SecurityContextHolder.getContext().setAuthentication(authenticate);

    sampleService.dashboard();
  }

  @Test
  @WithMockUser
  public void dashboard_mock() {

    // principal
//    Account account = new Account();
//    account.setRole("USER");
//    account.setUsername("keesun");
//    account.setPassword("123");
//    accountService.createNew(account);
//    final UserDetails userDetails = accountService.loadUserByUsername("keesun");
//
//    UsernamePasswordAuthenticationToken token
//            = new UsernamePasswordAuthenticationToken(userDetails, "123");
//
//    final Authentication authenticate = authenticationManager.authenticate(token);
//
//    SecurityContextHolder.getContext().setAuthentication(authenticate);

    sampleService.dashboard();
  }
}