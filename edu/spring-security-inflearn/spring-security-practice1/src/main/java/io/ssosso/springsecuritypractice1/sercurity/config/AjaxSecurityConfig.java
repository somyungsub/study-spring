package io.ssosso.springsecuritypractice1.sercurity.config;

import io.ssosso.springsecuritypractice1.sercurity.common.AjaxLoginAuthenticationEntryPoint;
import io.ssosso.springsecuritypractice1.sercurity.filter.*;
import io.ssosso.springsecuritypractice1.sercurity.handler.AjaxAccessDeniedHandler;
import io.ssosso.springsecuritypractice1.sercurity.handler.AjaxAuthenticationFailureHandler;
import io.ssosso.springsecuritypractice1.sercurity.handler.AjaxAuthenticationSuccessHandler;
import io.ssosso.springsecuritypractice1.sercurity.provider.AjaxAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@EnableWebSecurity
//@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(ajaxAuthenticationProvider());
  }

  private AuthenticationProvider ajaxAuthenticationProvider() {
    return new AjaxAuthenticationProvider(userDetailsService, passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .antMatcher("/api/**")
      .authorizeRequests()
      .antMatchers("/api/messages").hasRole("MANAGER")
      .antMatchers("/api/login").permitAll()
      .anyRequest().authenticated()
    .and()
      // UsernamePasswordAuthenticationFilter 전에 ajaxLoginProcessingFilter를 위치시킨다
      .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

    http
      .exceptionHandling()
      .authenticationEntryPoint(ajaxLoginAuthenticationEntryPoint())
      .accessDeniedHandler(ajaxAccessDeniedHandler())
    ;

    http
      .csrf()
//      .disable() // csrf 토큰 검사 비활성화

    ;
  }

  @Bean
  public AccessDeniedHandler ajaxAccessDeniedHandler() {
    return new AjaxAccessDeniedHandler();
  }

  @Bean
  public AuthenticationEntryPoint ajaxLoginAuthenticationEntryPoint() {
    return new AjaxLoginAuthenticationEntryPoint();
  }

  @Bean
  public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
    AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
    ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
    ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
    ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());

    return ajaxLoginProcessingFilter;
  }

  @Bean
  public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
    return new AjaxAuthenticationSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
    return new AjaxAuthenticationFailureHandler();
  }
}
