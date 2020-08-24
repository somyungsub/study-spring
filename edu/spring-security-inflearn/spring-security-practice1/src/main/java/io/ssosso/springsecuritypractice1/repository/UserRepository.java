package io.ssosso.springsecuritypractice1.repository;

import io.ssosso.springsecuritypractice1.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}
