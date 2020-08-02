package me.ssosso.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity  // 웹시큐리티 설정 클래스들 임포트 시키는 애노테이션이므로 필수로 선언해야함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

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
//        .loginPage("/loginPage")      // 로그인 페이지 경로
//        .defaultSuccessUrl("/")       // 로그인 성공시 기본 URL
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
        .permitAll();  // 모든 사용자 접근 가능함 로그인 URL은


    // 로그아웃
    http
        .logout()
        .logoutUrl("/logout") // 디폴트 post 방식, get은 에러 -> 설정 필요
        .logoutSuccessUrl("/login")
        .addLogoutHandler(new LogoutHandler() {
          @Override
          public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
            HttpSession session = httpServletRequest.getSession();
            session.invalidate(); // 세션초기화
          }
        })
        .logoutSuccessHandler(new LogoutSuccessHandler() {
          @Override
          public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
            httpServletResponse.sendRedirect("/login");
          }
        })
        .deleteCookies("remember-me") // 쿠키 삭제
    ;

    http
        .rememberMe() // 기본은 remember-me
        .rememberMeParameter("remember")
        .tokenValiditySeconds(3600) // 1시간으로 설정 (기본 14일)
        .userDetailsService(userDetailsService)
    ;



  }
}
