package io.ssosso.springsecuritypractice1.sercurity.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FormAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  /*
    인증 검증 중 실패 -> 인증예외 발생
    - provider, details 검증 실패
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//    super.onAuthenticationFailure(request, response, exception);

    String errorMessage = "Invalid Username or Password";

    if (exception instanceof BadCredentialsException) {
      errorMessage = "Invalid Username or Password";
    } else if (exception instanceof InsufficientAuthenticationException) {
      errorMessage = "Invalid Secret Key";
    } else if (exception instanceof DisabledException) {
      errorMessage = "Locked";
    } else if (exception instanceof CredentialsExpiredException) {
      errorMessage = "Expired password";
    }

    setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);
    super.onAuthenticationFailure(request, response, exception);
  }
}
