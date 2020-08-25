package io.ssosso.springsecuritypractice1.sercurity.common;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class FormWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

  @Override
  public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
    // 커스텀
    return new FormWebAuthenticationDetails(request);
  }

}
