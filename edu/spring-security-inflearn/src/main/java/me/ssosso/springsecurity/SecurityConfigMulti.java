package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity
@Order(0) // 먼저
public class SecurityConfigMulti extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .antMatcher("/admin/**")
        .authorizeRequests()
        .anyRequest().authenticated()
    .and()
        .httpBasic();// 인증방식 -> http 기본인증
  }
}


@Configuration
@Order(1)
class SecurityConfigMulti2 extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .anyRequest().permitAll()
    .and()
        .formLogin();// 인증방식 -> form로그인
  }
}
