package io.ssosso.rest.accounts;

import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountRepository;
import com.example.demo.accounts.AccountRole;
import com.example.demo.accounts.AccountService;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  public void findByUsername() throws Exception {

    // Given
    String username = "ssosso@gmail.com";
    String password = "userDetails";
    Account account
        = Account.builder()
        .email(username)
        .password(password)
        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
        .build()
        ;

//    this.accountRepository.save(account);
    // 시큐리티 적용
    this.accountService.saveAccount(account);

    // When
    UserDetailsService userDetailsService = (UserDetailsService) accountService;
    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // Then
//    assertThat(userDetails.getPassword()).isEqualTo(password);
    // 시큐리티 적용 테스트
    assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
  }

//  @Test(expected = UsernameNotFoundException.class)
  @Test
  public void findByUsernameFail() {
    String username = "random@email.com";

    try {
      accountService.loadUserByUsername(username);
      fail("supported to be failed");
    } catch (UsernameNotFoundException e) {
      assertThat(e instanceof UsernameNotFoundException).isTrue();
      assertThat(e.getMessage()).containsSequence(username);
    }
  }
  @Test
  public void findByUsernameFailRule() {

    // Expected
    String username = "random@email.com";
    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage(Matchers.containsString(username));

    // When
    accountService.loadUserByUsername(username);

    // Then -> 안됨, 미리 예상을 해야됨
//    expectedException.expect(UsernameNotFoundException.class);
//    expectedException.expectMessage(Matchers.containsString(username));

  }

}