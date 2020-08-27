package io.ssosso.springsecuritypractice1.controller.login;

import io.ssosso.springsecuritypractice1.domain.entity.Account;
import io.ssosso.springsecuritypractice1.sercurity.service.AccountContext;
import io.ssosso.springsecuritypractice1.sercurity.token.AjaxAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "exception", required = false) String exception,
                      Model model) {

    model.addAttribute("error", error);
    model.addAttribute("exception", exception);

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

  @GetMapping("/denied")
  public String denied(@RequestParam(value = "exception", required = false) String exception,
                       Principal principal,
                       Model model) {

//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    Account account = (Account) authentication.getPrincipal();

    Account account = null;
    if (principal instanceof UsernamePasswordAuthenticationToken) {
      account = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

    }else if(principal instanceof AjaxAuthenticationToken){
      account = (Account) ((AjaxAuthenticationToken) principal).getPrincipal();
    }

    model.addAttribute("username", account.getUsername());
    model.addAttribute("exception", exception);

    return "user/login/denied";
  }
}
