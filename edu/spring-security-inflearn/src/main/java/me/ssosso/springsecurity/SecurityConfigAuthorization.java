package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfigAuthorization extends WebSecurityConfigurerAdapter {

  /*
    인가처리
    - AccessDecisionManager
    - AccessDecisionVoter 3 가지 유형
   */

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/user").hasRole("USER")
        .anyRequest().permitAll();

    http
        .formLogin();
  }
}
