package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity  // 웹시큐리티 설정 클래스들 임포트 시키는 애노테이션이므로 필수로 선언해야함
public class SecurityConfig2 extends WebSecurityConfigurerAdapter {

  // http 요청에 대한 시큐리티 설정
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 인가 정책
    http
        .authorizeRequests()
        .anyRequest().authenticated();


    // 인증 정책
    http
        .formLogin();


    // 동시세션 제어
    http
        .sessionManagement()
        .maximumSessions(1)             // 최대 허용 가능 세션수
        .maxSessionsPreventsLogin(false) // 동시로그인 차단(true), false : 기존세션만료(default)
    ;

    // 세션 고정 보호
    http
        .sessionManagement()
        .sessionFixation()
//        .none() // 공격에 취약함... 세션 ID 탈취 되면 ... 정보 공유되어 로그인됨
        .changeSessionId() // 기본값 -> 새롭게 세션ID 발급됨

    ;





  }
}
