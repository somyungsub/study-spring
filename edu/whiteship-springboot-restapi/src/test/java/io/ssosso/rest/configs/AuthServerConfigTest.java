package io.ssosso.rest.configs;


import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountRole;
import com.example.demo.accounts.AccountService;
import com.example.demo.common.TestDescription;
import io.ssosso.rest.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {
  @Autowired
  AccountService accountService;

  @Test
  @TestDescription("인증 토큰 발급 받는 테스트")
  public void getAuthToken() throws Exception {

    // Given
    final String username = "ssosso.dev@gmail.com";
    final String password = "sso";
    final Account sso = Account.builder()
        .email(username)
        .password(password)
        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
        .build();

    this.accountService.saveAccount(sso);

    String clientId = "myApp";
    String clientSecret = "pass";

    this.mockMvc.perform(post("/oauth/token")
          .with(httpBasic(clientId, clientSecret))
          .param("username", username)
          .param("password", password)
          .param("grant_type", "password")  // 인증타입 -> password 인증
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("access_token").exists());
  }

}