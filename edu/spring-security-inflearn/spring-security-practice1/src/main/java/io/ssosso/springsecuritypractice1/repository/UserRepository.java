package io.ssosso.springsecuritypractice1.repository;

import io.ssosso.springsecuritypractice1.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
  Account findByUsername(String username);
  int countByUsername(String username);
}
