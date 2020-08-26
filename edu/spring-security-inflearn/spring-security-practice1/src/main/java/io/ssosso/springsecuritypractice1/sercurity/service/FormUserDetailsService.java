package io.ssosso.springsecuritypractice1.sercurity.service;

import io.ssosso.springsecuritypractice1.domain.Account;
import io.ssosso.springsecuritypractice1.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class FormUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public FormUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = userRepository.findByUsername(username);

    if (account == null) {
      throw new UsernameNotFoundException("UsernameNotFoundException");
    }

    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(account.getRole()));

    return new AccountContext(account, roles);
  }
}
