package io.sso.demospringsecurity.account;

import org.springframework.data.jpa.repository.JpaRepository;

// 알아서 Bean 등록 됨
public interface AccountRepository extends JpaRepository<Account, Integer> {

  Account findByUsername(String username);
}
