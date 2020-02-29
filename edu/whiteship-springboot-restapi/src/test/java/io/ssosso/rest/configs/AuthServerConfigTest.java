package io.ssosso.rest.configs;


import io.ssosso.rest.accounts.Account;
import io.ssosso.rest.accounts.AccountRole;
import io.ssosso.rest.accounts.AccountService;
import io.ssosso.rest.common.AppProperties;
import io.ssosso.rest.common.BaseControllerTest;
import io.ssosso.rest.common.TestDescription;
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

  @Autowired
  AppProperties appProperties;

  @Test
  @TestDescription("인증 토큰 발급 받는 테스트")
  public void getAuthToken() throws Exception {

    // Given
    final Account sso = Account.builder()
        .email(appProperties.getUserUsername())
        .password(appProperties.getUserPassword())
        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
        .build();

    this.accountService.saveAccount(sso);


    this.mockMvc.perform(post("/oauth/token")
          .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
          .param("username", appProperties.getUserUsername())
          .param("password", appProperties.getUserPassword())
          .param("grant_type", "password")  // 인증타입 -> password 인증
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("access_token").exists());
  }

}