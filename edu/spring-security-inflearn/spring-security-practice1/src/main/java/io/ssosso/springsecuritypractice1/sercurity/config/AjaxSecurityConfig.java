package io.ssosso.springsecuritypractice1.sercurity.config;

import io.ssosso.springsecuritypractice1.filter.AjaxLoginProcessingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .antMatcher("/api/**")
      .authorizeRequests()
      .anyRequest().authenticated()
    .and()
      // UsernamePasswordAuthenticationFilter 전에 ajaxLoginProcessingFilter를 위치시킨다
      .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
      .csrf().disable() // csrf 토큰 검사 비활성화
    ;
  }

  @Bean
  public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
    AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
    ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
    return ajaxLoginProcessingFilter;
  }
}
