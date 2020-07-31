package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity  // 웹시큐리티 설정 클래스들 임포트 시키는 애노테이션이므로 필수로 선언해야함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // http 요청에 대한 시큐리티 설정
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 인가 정책
    http
        .authorizeRequests()
        .anyRequest().authenticated();


    // 인증 정책
    http
        .formLogin()
        .loginPage("/loginPage")      // 로그인 페이지 경로
        .defaultSuccessUrl("/")       // 로그인 성공시 기본 URL
        .failureUrl("/login")         // 실패시 login 페이지 보이기
        .usernameParameter("userId")  // 요청 파라미터
        .passwordParameter("passwd")  // 요청 파라미터
        .loginProcessingUrl("/login_proc")  // 로그인 Form Action URL
        .successHandler(new AuthenticationSuccessHandler() {
          @Override
          public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
            System.out.println("authentication" + authentication.getName());
            httpServletResponse.sendRedirect("/");
          }
        }) // 인증성공시, 콜백 호출
        .failureHandler(new AuthenticationFailureHandler() {
          @Override
          public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
            System.out.println("exception " + e.getMessage());
            httpServletResponse.sendRedirect("/login");
          }
        })
        .permitAll()  // 모든 사용자 접근 가능함 로그인 URL은
    ;

  }
}
