package io.sso.demospringsecurity.form;


import io.sso.demospringsecurity.account.AccountContext;
import io.sso.demospringsecurity.account.AccountRepository;
import io.sso.demospringsecurity.common.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.concurrent.Callable;

@Controller
public class SampleController {

  @Autowired
  SampleService sampleService;

  @Autowired
  AccountRepository accountRepository;

  @GetMapping("/")
  public String index(Model model, Principal principal) {

    if (principal == null) {
      model.addAttribute("message", "Hello Spring Security");
    } else {
      model.addAttribute("message", "Hello, " + principal.getName());
    }
    return "index";
  }

  @GetMapping("/info")
  public String info(Model model) {
    model.addAttribute("message", "Info");
    return "info";
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model, Principal principal) {
    model.addAttribute("message", "Hello" + principal.getName());
    AccountContext.setAccount(accountRepository.findByUsername(principal.getName()));
    sampleService.dashboard();
    return "dashboard";
  }

  @GetMapping("/admin")
  public String admin(Model model, Principal principal) {
    model.addAttribute("message", "Hello Admin" + principal.getName());
    return "admin";
  }

  @GetMapping("/user")
  public String user(Model model, Principal principal) {
    model.addAttribute("message", "Hello User" + principal.getName());
    return "user";
  }

  @GetMapping("/async-handler")
  @ResponseBody
  public Callable<String> asyncHandler() {
    SecurityLogger.log("MVC");  // Thread 다름 , nio - tomcat 할당 쓰레드
    return new Callable<String>() {
      @Override
      public String call() throws Exception {
        // Thread 다름 -> task 쓰레드
        SecurityLogger.log("Callable");
        return "Async Handler";
      }
    };
  }
}
