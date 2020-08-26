package io.ssosso.springsecuritypractice1.sercurity.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {

    String errorMessage = "Invalid Username or Password";

    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

    if (exception instanceof BadCredentialsException) {
      errorMessage = "Invalid Username or Password";
    } else if (exception instanceof InsufficientAuthenticationException) {
      errorMessage = "Invalid Secret Key";
    } else if (exception instanceof DisabledException) {
      errorMessage = "Locked";
    } else if (exception instanceof CredentialsExpiredException) {
      errorMessage = "Expired password";
    }

    // body 에 메시지가 담겨서 전달됨
    objectMapper.writeValue(httpServletResponse.getWriter(), errorMessage);
  }
}
