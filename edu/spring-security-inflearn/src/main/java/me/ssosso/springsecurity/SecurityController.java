package me.ssosso.springsecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SecurityController {

  @GetMapping("/")
  public String index(HttpSession httpSession) {

    // 저장된 인증 객체
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // HttpSession을 이용해서 인증 객체 받을 수 있음
    SecurityContext securityContext = (SecurityContext)httpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    Authentication authentication2 = securityContext.getAuthentication();

    return "home";
  }

  @GetMapping("/thread")
  public String thread() {
    new Thread(
        () -> {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          System.out.println("authentication = " + authentication);
        }
    ).start();

    return "thread";
  }

  @GetMapping("loginPage")
  public String loginPage() {
    return "loginPage";
  }

  @GetMapping("/user")
  public String user(){
    return "user";
  }

  @GetMapping("/admin/pay")
  public String adminPay(){
    return "adminPay";
  }

  @GetMapping("/admin/**")
  public String admin(){
    return "admin";
  }

  @GetMapping("/denied")
  public String denied(){
    return "denied";
  }

  @GetMapping("/login")
  public String login(){
    return "login";
  }

}
