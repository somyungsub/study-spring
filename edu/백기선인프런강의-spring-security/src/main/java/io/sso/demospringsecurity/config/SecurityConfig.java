package io.sso.demospringsecurity.config;

import io.sso.demospringsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
            .mvcMatchers("/user").hasAuthority("ROLE_USER") // /user 요청 인증 + USer 롤만 허용
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


    http.rememberMe()
            .alwaysRemember(false)
            .useSecureCookie(true)  // 노출 안되게 허용
            .rememberMeParameter("remember")  // default remember-me
            .userDetailsService(accountService)
            .key("remember-me-sample")
    ;


    http.httpBasic();    // Http 기본 설정 정보 사용, base64로 인코딩

    http.logout()
//            .logoutUrl("/my/logout")
            .logoutSuccessUrl("/")  // 성공후 리다이렉트 경로
//            .addLogoutHandler()
//            .logoutSuccessHandler()  // 성공후
//            .invalidateHttpSession(true)
//            .deleteCookies()  // 쿠키 삭제
    ;

//    http.anonymous().principal("anonymousUser");

    http.sessionManagement()
            .sessionFixation()
              .changeSessionId()  // 세션 아이디 변경하기
            .maximumSessions(1)    // 세션 유지 갯수
//              .expiredUrl()     // 세션 만료후 보낼
              .maxSessionsPreventsLogin(false)  // 새로운 세션 로그인을 막는 기능
//            .invalidSessionUrl()
   ;

    // Spring Session 프로젝트 사용검토
    http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 전략, 무상태 > 세션 사용 적절하지 않음

    ;

    // ExceptionTranslatorFilter -> FilterSecurityInterceptor (AccessDecisionManager, AffirmativeBased)
    // 1. AuthenticationException -> 인증이 안된 경우, AuthenticationEntryPoint
    // 2. AccessDeniedException   -> 인가가 안된 경우, AccessDeniedHandler
    http.exceptionHandling()
            .accessDeniedPage("/access-denied")

            // 좀더 다양한 컨트롤이 필요한 경우,
            .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
              UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
              // Logger 사용으로 .. 바꾸기 편의상
              System.out.println("principal.getUsername() = " + principal.getUsername());
              System.out.println("httpServletRequest.getRequestURL() = " + httpServletRequest.getRequestURL());
              httpServletResponse.sendRedirect("/access-denied");
            })
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
