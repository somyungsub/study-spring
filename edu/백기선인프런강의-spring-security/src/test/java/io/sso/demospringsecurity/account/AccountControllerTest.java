package io.sso.demospringsecurity.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  AccountService accountService;

  @Test
  public void index_anonymous() throws Exception{
    mockMvc.perform(get("/").with(anonymous()))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithAnonymousUser
  public void index_anonymous2() throws Exception{
    mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void index_user() throws Exception{
    mockMvc.perform(get("/").with(user("keesun").roles("USER")))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "keesun", roles = "USER")
  public void index_user2() throws Exception{
    mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUser
  public void index_user_custom_annotation() throws Exception{
    mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void index_admin() throws Exception{
    mockMvc.perform(get("/").with(user("admin").roles("ADMIN")))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void admin_user() throws Exception{
    mockMvc.perform(get("/admin").with(user("keesun").roles("USER")))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "keesun", roles = "USER")
  public void admin_user2() throws Exception{
    mockMvc.perform(get("/admin"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  public void admin_admin() throws Exception{
    mockMvc.perform(get("/admin").with(user("keesun").roles("ADMIN")))
        .andDo(print())
        .andExpect(status().isOk());
  }
  @Test
  @WithMockUser(username = "keesun", roles = "ADMIN")
  public void admin_admin2() throws Exception{
    mockMvc.perform(get("/admin"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  // DB 접근하는 경우 -> @Transactional 추가하여 단일 테스트에 대해 RollBack 필요
  // JDK, Spring 둘다 가능 -> Spring이 좀더 많은 기능을 제공하므로 스프링꺼 사용
  @Test
  @javax.transaction.Transactional
  public void form_login_success() throws Exception {

    final String username = "keesun";
    final String password = "123";
    final Account user = createUser(username, password);
    mockMvc.perform(formLogin().user(user.getUsername()).password(password))
        .andExpect(authenticated());
  }

  @Test
  @Transactional
  public void form_login_success_tran() throws Exception {

    final String username = "keesun";
    final String password = "123";
    final Account user = createUser(username, password);
    mockMvc.perform(formLogin().user(user.getUsername()).password(password))
        .andExpect(authenticated());
  }

  @Test
  @Transactional
  public void form_login_fail() throws Exception {

    final String username = "keesun";
    final String password = "123";
    final Account user = createUser(username, password);
    mockMvc.perform(formLogin().user(user.getUsername()).password("1234"))
        .andExpect(unauthenticated());
  }

  private Account createUser(String username, String password) {
    Account account = new Account();
    account.setUsername(username);
    account.setPassword(password);
    account.setRole("USER");
    return accountService.createNew(account);
  }


}