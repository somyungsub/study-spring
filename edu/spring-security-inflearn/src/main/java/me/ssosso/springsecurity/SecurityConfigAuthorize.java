package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity  // 웹시큐리티 설정 클래스들 임포트 시키는 애노테이션이므로 필수로 선언해야함
public class SecurityConfigAuthorize extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .inMemoryAuthentication() // 메모리 방식으로 사용자 생성 -> 사용자 생성 갯수 제한 없음
        .withUser("user").password("{noop}1111").roles("USER");  // noop:알고리즘 사용x
    auth
        .inMemoryAuthentication()
//        .withUser("sys").password("{noop}1111").roles("SYS");
        .withUser("sys").password("{noop}1111").roles("SYS","USER");
    auth
        .inMemoryAuthentication()
//        .withUser("admin").password("{noop}1111").roles("ADMIN");
        .withUser("admin").password("{noop}1111").roles("ADMIN", "SYS", "USER");  // 권한 계층 (롤 하이라키) 기능으로 처리해야함

  }

  // http 요청에 대한 시큐리티 설정
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 인가 정책
    http
        .authorizeRequests()  // 모든 요청
        .antMatchers("/user").hasRole("USER") // /user 요청 -> USER 권한 필요
        .antMatchers("/admin/pay").hasRole("ADMIN")
        .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
        .anyRequest() // 모든 요청에 대해서는
        .authenticated(); // 인증된 사용자만 자원에 접근 허용

    // 인증 정책
    http
        .formLogin();





  }
}
