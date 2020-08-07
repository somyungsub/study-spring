package me.ssosso.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        .antMatchers("/login").permitAll()
        .antMatchers("/user").hasRole("USER") // /user 요청 -> USER 권한 필요
        .antMatchers("/admin/pay").hasRole("ADMIN")
        .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
        .anyRequest() // 모든 요청에 대해서는
        .authenticated(); // 인증된 사용자만 자원에 접근 허용

    // 인증 정책
    http
        .formLogin()
        .successHandler(new AuthenticationSuccessHandler() {
          @Override
          public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
            // 캐시 저장된 정보 재사용
            RequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
            String redirectUrl = savedRequest.getRedirectUrl();
            httpServletResponse.sendRedirect(redirectUrl);
          }
        })
    ;

    // 얘외처리
    http
        .exceptionHandling()
//        .authenticationEntryPoint(new AuthenticationEntryPoint() {
//          @Override
//          public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//            httpServletResponse.sendRedirect("/login"); // 사용자 정의 /login로 매핑됨
//          }
//        })
        .accessDeniedHandler(new AccessDeniedHandler() {
          @Override
          public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
            httpServletResponse.sendRedirect("/denied");
          }
        })

        ;





  }
}
