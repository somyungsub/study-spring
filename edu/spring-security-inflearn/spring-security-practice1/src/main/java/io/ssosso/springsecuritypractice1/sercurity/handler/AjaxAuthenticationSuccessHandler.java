package io.ssosso.springsecuritypractice1.sercurity.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ssosso.springsecuritypractice1.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

    // 저장된 인증 객체 추출
    Account account = (Account) authentication.getPrincipal();

    httpServletResponse.setStatus(HttpStatus.OK.value());
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

    objectMapper.writeValue(httpServletResponse.getWriter(), account);
  }
}
