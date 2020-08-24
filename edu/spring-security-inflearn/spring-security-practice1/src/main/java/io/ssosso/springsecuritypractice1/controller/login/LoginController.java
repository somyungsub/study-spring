package io.ssosso.springsecuritypractice1.controller.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    // 저장된 인증객체 -> 얻기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      // 로그아웃처리 -> 내부적으로 clearContext()실행 됨 -> 세션에서 제거 시킴
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    return "redirect:/login";
  }
}
