package io.ssosso.rest.configs;


import io.ssosso.rest.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  AccountService accountService;

  @Autowired
  PasswordEncoder passwordEncoder;

  // 토큰 저장소
  @Bean
  public TokenStore tokenStore(){
    return new InMemoryTokenStore();
  }

  // 오버라이딩 -> 빈등록
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  // 보안 적용 대상 등록
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(accountService)
        .passwordEncoder(passwordEncoder);
  }

  // 웹시큐리티 필터 적용여부 결정
//  @Override
//  public void configure(WebSecurity web) throws Exception {
//
//    // 무시하겠다
//    web.ignoring().mvcMatchers("/docs/index.html");
//
//    // 정적파일 무시
//    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//  }

  // Http를 이용한 필터
  @Override
  public void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests()
//        .mvcMatchers("/docs/index.html").anonymous()
//        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//    ;

    /*
        익명허용
        form 로그인 허용
        GET /api/** 요청 인증
     */
    http
        .anonymous()
        .and()
        .formLogin()
        .and()
        .authorizeRequests()
        .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
        .anyRequest()
        .authenticated()
    ;

  }

}
