package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity  // 웹시큐리티 설정 클래스들 임포트 시키는 애노테이션이므로 필수로 선언해야함
public class SecurityConfig3 extends WebSecurityConfigurerAdapter {

  // http 요청에 대한 시큐리티 설정
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 인가 정책
    http
        .authorizeRequests()
        .anyRequest().authenticated();


    // 인증 정책,
    http
        .formLogin()
        .and()
        .sessionManagement()
        .maximumSessions(1) // 최대 세션수 설정 -> 1
//        .maxSessionsPreventsLogin(true) // 1번째 방식 : 예외 처리
        .maxSessionsPreventsLogin(false)  // 2번째 방식 : 세션 만료 -> 로그아

    ;







  }
}
