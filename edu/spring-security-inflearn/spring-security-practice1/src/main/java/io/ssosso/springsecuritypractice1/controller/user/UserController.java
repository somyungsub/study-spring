package io.ssosso.springsecuritypractice1.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

  @GetMapping("/mypage")
  public String myPage(){

    return "user/mypage";
  }
}
