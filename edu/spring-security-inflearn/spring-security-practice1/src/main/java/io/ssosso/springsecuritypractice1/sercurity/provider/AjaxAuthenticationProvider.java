package io.ssosso.springsecuritypractice1.sercurity.provider;

import io.ssosso.springsecuritypractice1.sercurity.common.FormWebAuthenticationDetails;
import io.ssosso.springsecuritypractice1.sercurity.service.AccountContext;
import io.ssosso.springsecuritypractice1.sercurity.token.AjaxAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AjaxAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  public AjaxAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 검증을 위한 구현
    // authentication -> 매니저로 부터 전달받은 인증위한 객체
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    // CustomUserDetailsService 에서 반환되는 객체 (DB에 저장된 객체)
    AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

    // DB에 저장된 객체와 전달된 객체의 검증 -> 일치 하지 않으면 -> 인증 실패 처리
    if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
      throw new BadCredentialsException("Authentication fail.");
    }

    // 시크릿키 전달 여부 확인 검증
    FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
    String secretKey = formWebAuthenticationDetails.getSecretKey();
    if (secretKey == null || !"secret".equals(secretKey)) {
      throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
    }

    // 인증된거면 -> 토큰 만듬 / 생성자 매개변수 확인 / 인증 된 객체를 만들어서 반환
    AjaxAuthenticationToken authenticationToken
      = new AjaxAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());

    // 최종적으로 인증에 성공한 객체를 반환함
    return authenticationToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    // 토큰타입이 일치할 때, 인증처리를 할 수 있도록 !
    return authentication.equals(AjaxAuthenticationToken.class);
  }

}
