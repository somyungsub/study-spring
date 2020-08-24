package io.ssosso.springsecuritypractice1.sercurity.config;

import io.ssosso.springsecuritypractice1.sercurity.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    // 사용자 추가
//    String password = passwordEncoder().encode("1111");
//
//    auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
//    auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER", "USER");
//    auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN", "USER", "MANAGER");
//  }

  // 커스텀 작성한 UserDetailsService -> CustomUser~ 주입
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private AuthenticationDetailsSource authenticationDetailsSource;

  @Bean
  public PasswordEncoder passwordEncoder() {
    // 평문 -> 암호화 처리
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 커스텀 userDetailsService 등록 -> 인증객체 만들기 (DB 비교 -> 인증? -> ok -> 인증객체(AccountContext) 만들어 반환)
    // JWT토큰 인증 ? -> 토큰관련 인증객체를 사용
    auth.userDetailsService(userDetailsService);
    // 커스텀 provider 등록 -> 검증처리하기
    auth.authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder()));
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // 정적파일 -> 보안필터 적용 무시 (enum -> StaticResourceLocation 내용 확인)
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .antMatchers("/","/users","user/login/**").permitAll()
      .antMatchers("/mypage").hasRole("USER")
      .antMatchers("/messages").hasRole("MANAGER")
      .antMatchers("/config").hasRole("ADMIN")
      .anyRequest().authenticated();

    http
      .formLogin()
      .loginPage("/login")
      .loginProcessingUrl("/login_proc")  // login.html -> login_proc (폼 액션 부분 일치시켜야함)
      .authenticationDetailsSource(authenticationDetailsSource)
      .defaultSuccessUrl("/")             // 로그인 성공 후 이동
      .permitAll()

    ;

  }
}
