package io.ssosso.springsecuritypractice1.service.impl;

import io.ssosso.springsecuritypractice1.domain.Account;
import io.ssosso.springsecuritypractice1.repository.UserRepository;
import io.ssosso.springsecuritypractice1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Transactional
  @Override
  public void createUser(Account account) {
    userRepository.save(account);
  }
}
