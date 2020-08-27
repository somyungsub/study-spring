package io.ssosso.springsecuritypractice1.service;

import io.ssosso.springsecuritypractice1.domain.entity.Account;

public interface UserService {

  void createUser(Account account);
}
