package io.ssosso.springsecuritypractice1.sercurity;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 사용자 추가
    String password = passwordEncoder().encode("1111");

    auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
    auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER", "USER");
    auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN", "USER", "MANAGER");
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // 정적파일 -> 보안필터 적용 무시 (enum -> StaticResourceLocation 내용 확인)
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // 평문 -> 암호화 처리
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .antMatchers("/").permitAll()
      .antMatchers("/mypage").hasRole("USER")
      .antMatchers("/messages").hasRole("MANAGER")
      .antMatchers("/config").hasRole("ADMIN")
      .anyRequest().authenticated();

    http.formLogin();
  }
}
