package io.sso.demospringsecurity.config;

import io.sso.demospringsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE - 50)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  AccountService accountService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

//    http
//        .antMatcher("/account/**")
//        .authorizeRequests()
//        .anyRequest().permitAll();
//
//    http.formLogin();
//    http.httpBasic();

    // 요청
    http
            .antMatcher("/**")
            .authorizeRequests()
            .mvcMatchers("/", "/info", "/account/**").permitAll()  // /info 요청 모든 등급 허용, 로그인 x
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

//    // 메모리 DB에 들어가는 값
//    auth.inMemoryAuthentication()
//        .withUser("keesun").password("{noop}123").roles("USER").and()
//        .withUser("admin").password("{noop}!@#").roles("ADMIN");

    // 구현체 등록 하여 사용하게 하기 -> 하지 않아도, Bean으로 등록되어 있으면 알아서 가져다가 사용함
    auth.userDetailsService(accountService);
  }
}
