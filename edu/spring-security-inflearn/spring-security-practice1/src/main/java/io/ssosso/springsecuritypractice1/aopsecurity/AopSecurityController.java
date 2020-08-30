package io.ssosso.springsecuritypractice1.aopsecurity;

import io.ssosso.springsecuritypractice1.domain.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AopSecurityController {

  @Autowired
  private AopMethodService aopMethodService;

  @GetMapping("/preAuthorize")
  @PreAuthorize("hasRole('ROLE_USER') and #account.username == principal.name")
  public String preAuthorize(AccountDto account, Model model, Principal principal) {
    model.addAttribute("method", "Success @PreAuthorize");
    return "aop/method";
  }

  @GetMapping("/method")
  public String methodSecured(Model model) {
    aopMethodService.methodSecured();
    model.addAttribute("method", "Success MethodSecured");
    return "aop/method";
  }

}
