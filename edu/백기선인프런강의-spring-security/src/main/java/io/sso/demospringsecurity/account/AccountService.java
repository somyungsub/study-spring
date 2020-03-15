package io.sso.demospringsecurity.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService { // Spring Security 제공, RDBMS, NoSQL 제약없음


  // TODO {noop}123 -> {noop} 암호화 알고리즘이 들어가는 정보
  @Autowired
  AccountRepository accountRepository;  // 임의 DAO 구현체가 와도 무방함. 상관없음, 지금은 JPA

  /*
    username -> user정보를 디비에서 갖고와서  UserDetails로
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // User 정보 조회
    final Account account = accountRepository.findByUsername(username);

    if (account == null) {
      throw new UsernameNotFoundException(username);
    }

    // 타입 변환 필요 (Account -> UserDetails)
    return User.builder()
        .username(account.getUsername())
        .password(account.getPassword())
        .roles(account.getRole())
        .build();
  }


  public Account createNew(Account account) {

    // Spring Security 포맷에 맡게 인코딩해주기
    // account.setPassword("{noop}" + account.getPassword());
    account.encodePassword();
    return accountRepository.save(account);
  }

}
