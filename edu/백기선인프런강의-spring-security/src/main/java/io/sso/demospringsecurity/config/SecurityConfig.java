package io.sso.demospringsecurity.config;

import io.sso.demospringsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Configuration
//@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE - 50)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  AccountService accountService;

  public AccessDecisionManager accessDecisionManager() {
    // 정의를 하지 않으면 기본 생성 (AffirmativeBased)


    // Role 정의
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER"); // Role 하이라키

    // Handler 셋팅
    DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);

    // WebExpressionVoter 세팅
    WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
    webExpressionVoter.setExpressionHandler(handler);


    List<AccessDecisionVoter<? extends Object>> webExpressionVoters = Arrays.asList(webExpressionVoter);
    return new AffirmativeBased(webExpressionVoters);
  }

  public SecurityExpressionHandler expressionHandler() {
    // 정의를 하지 않으면 기본 생성 (AffirmativeBased)


    // Role 정의
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER"); // Role 하이라키

    // Handler 셋팅
    DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);

    return handler;
  }

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
            .authorizeRequests()
            .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()  // /info 요청 모든 등급 허용, 로그인 x
            .mvcMatchers("/admin").hasRole("ADMIN") // /admin 요청 인증 + ADMIN 롤만 허용
            .mvcMatchers("/user").hasRole("USER") // /user 요청 인증 + USer 롤만 허용
//            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 추천하지 않음, 설정된 모든 필터를 체크하게 됨
            .anyRequest().authenticated() // 나머지, 인증만 되면 허용
//            .accessDecisionManager(accessDecisionManager());
            .expressionHandler(expressionHandler());

    http.formLogin()    // 로그인 , form, oauth, openid
//        .usernameParameter("my-username")   // form -> name 값 설정
//        .passwordParameter("my-password")   // form -> name 값 설정
//        .loginPage("/signin")  // 로그인페이지 커스텀 설정
        .loginPage("/login")  //
        .permitAll()
    ;



    http.httpBasic();    // Http 기본 설정 정보 사용

    http.logout()
//            .logoutUrl("/my/logout")
            .logoutSuccessUrl("/")  // 성공후 리다이렉트 경로
//            .addLogoutHandler()
//            .logoutSuccessHandler()  // 성공후
//            .invalidateHttpSession(true)
//            .deleteCookies()  // 쿠키 삭제
    ;

    // 현재 쓰레드에서 -> 하위쓰레드도 시큐리티 컨텍스트 공유가 되도록함
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // 시큐리티 무시, login 폼까지 가지 않게 함 -> 서버 리소스를 먹지 않으므로 속도향상이 되긴함
//    web.ignoring().mvcMatchers("/favicon.ico");

    // 다양한 포맷 ignoring 할 수 있으므로 상황에 맞게 적절히 사용해야 함
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
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
