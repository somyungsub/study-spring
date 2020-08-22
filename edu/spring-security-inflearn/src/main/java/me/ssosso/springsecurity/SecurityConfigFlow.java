package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfigFlow extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    /*
      1. UsernamePasswordAuthenticationFilter
        - 인증 방식
      2. AuthenticationManager
        - providers : List
        - provider 에 위임
      3. AuthenticationProvider
        - DaoAuthenticationProvider -> 폼인증 방식 인증처리
          - UserDetailsService 호출 -> 조회 요청
        - 실제 인증처리
        - 유효성 검증
        - 인증 후 UserDetailsService
      4. UserDetailsService
        - 유저 객체 조회
        - User 반환
     */

    http
        .authorizeRequests()
        .anyRequest().authenticated();

    http
        .formLogin();
  }
}
