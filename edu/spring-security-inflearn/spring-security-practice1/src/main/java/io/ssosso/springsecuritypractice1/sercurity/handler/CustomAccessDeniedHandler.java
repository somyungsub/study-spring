package io.ssosso.springsecuritypractice1.sercurity.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private String errorPage;

  @Override
  public void handle(HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse,
                     AccessDeniedException e) throws IOException, ServletException {

    // 인가 예외 -> AccessDeniedException 파라미터
    // 메시지 및 페이지 전달

    String deniedUrl = errorPage + "?exception=" + e.getMessage();
    httpServletResponse.sendRedirect(deniedUrl);
  }

  public void setErrorPage(String errorPage) {
    this.errorPage = errorPage;
  }
}
