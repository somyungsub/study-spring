package io.sso.demospringsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 요청
    http.authorizeRequests()
            .mvcMatchers("/", "/info").permitAll()  // /info 요청 모든 등급 허용, 로그인 x
            .mvcMatchers("/admin").hasRole("ADMIN") // /admin 요청 인증 + ADMIN 롤만 허용
            .anyRequest().authenticated() // 나머지, 인증만 되면 허용
          .and()
        .formLogin()    // 로그인 , form, oauth, openid
          .and()
        .httpBasic()    // Http 기본 설정 정보 사용
    ;
  }

  // {noop} -> 암호화 저장방식 사용하지 않겠다. 값비교 일치 -> 인증
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    // 메모리 DB에 들어가는 값
    auth.inMemoryAuthentication()
        .withUser("keesun").password("{noop}123").roles("USER").and()
        .withUser("admin").password("{noop}!@#").roles("ADMIN");
  }
}
