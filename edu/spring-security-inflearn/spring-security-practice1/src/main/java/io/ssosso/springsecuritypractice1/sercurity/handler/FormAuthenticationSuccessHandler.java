package io.ssosso.springsecuritypractice1.sercurity.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  // 이전 요청에 관련된 정보 담겨있는 내용 참조할 객체
  private RequestCache requestCache = new HttpSessionRequestCache();

  //
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  /*
    여러가지의 후속작업이 가능함
    최종 인증 객체가 파라미터 Authentication 으로 전달 되고 있음
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//    super.onAuthenticationSuccess(request, response, authentication);

    // 디폴트 URL 설정
    setDefaultTargetUrl("/");

    // 인증 성공전 사용자의 요청에 대한 정보가 담긴 객체
    SavedRequest savedRequest = requestCache.getRequest(request, response);

    if (savedRequest != null) {
      // 인증 이전 다른 자원 접근 -> 예외 등... 일경우 null일 수 있음
      String targetUrl = savedRequest.getRedirectUrl();
      redirectStrategy.sendRedirect(request, response, targetUrl);
    } else {
      // null 인경우 -> 기본페이지 가도록 설정하기
      redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
    }

  }
}
