package io.ssosso.springsecuritypractice1.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ssosso.springsecuritypractice1.domain.AccountDto;
import io.ssosso.springsecuritypractice1.sercurity.token.AjaxAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.isEmpty;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

  private ObjectMapper objectMapper = new ObjectMapper();

  public AjaxLoginProcessingFilter() {
//    super(defaultFilterProcessesUrl);
    super(new AntPathRequestMatcher("/api/login")); // 이 URL 요청 -> 발동
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

    if (!isAjax(httpServletRequest)) {
      throw new IllegalStateException("Authentication is not supported");
    }

    AccountDto accountDto = objectMapper.readValue(httpServletRequest.getReader(), AccountDto.class);
    if (isEmpty(accountDto.getUsername()) || isEmpty(accountDto.getPassword())) {
      throw new IllegalArgumentException("Username or Password is empty");
    }

    AjaxAuthenticationToken ajaxAuthenticationToken
      = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

    // 인증처리 하도록 매니저에 전달하여 인증하도록 하기
    return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
  }

  private boolean isAjax(HttpServletRequest httpServletRequest) {

    // 이렇게 헤더값이 온다면 ? ->ajax 요청 약속
    if ("XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"))) {
      return true;
    }

    return false;
  }

}
