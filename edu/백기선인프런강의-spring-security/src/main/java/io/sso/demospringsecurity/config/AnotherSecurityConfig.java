package io.sso.demospringsecurity.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 15)
public class AnotherSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 요청
    http
        .antMatcher("/account/**")
        .authorizeRequests()
        .anyRequest().permitAll();

//    http.formLogin();    // 로그인 , form, oauth, openid
//    http.httpBasic();    // Http 기본 설정 정보 사용


  }
}
