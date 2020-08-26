package io.ssosso.springsecuritypractice1.sercurity.common;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

  /*
    인증예외 전달 됨
   */
  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

    // 인증을 받지 않은 상태로 접근 한 경우!
    // 401 에러 반환
    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
  }
}
