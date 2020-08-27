package io.ssosso.springsecuritypractice1.sercurity.service;

import io.ssosso.springsecuritypractice1.domain.entity.Account;
import io.ssosso.springsecuritypractice1.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

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

//    List<GrantedAuthority> roles = new ArrayList<>();
//    roles.add(new SimpleGrantedAuthority(account.getUserRoles()));

    //    return new AccountContext(account, roles);

    Set<String> userRoles = account.getUserRoles()
      .stream()
      .map(userRole -> userRole.getRoleName())
      .collect(toSet());

    List<GrantedAuthority> collect = userRoles.stream().map(SimpleGrantedAuthority::new).collect(toList());

    return new AccountContext(account, collect);

  }
}
