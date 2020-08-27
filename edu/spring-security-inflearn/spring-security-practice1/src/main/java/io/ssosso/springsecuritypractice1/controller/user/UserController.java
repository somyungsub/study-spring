package io.ssosso.springsecuritypractice1.controller.user;

import io.ssosso.springsecuritypractice1.domain.dto.AccountDto;
import io.ssosso.springsecuritypractice1.domain.entity.Account;
import io.ssosso.springsecuritypractice1.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  public UserController(PasswordEncoder passwordEncoder, UserService userService) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }

  @GetMapping("/mypage")
  public String myPage() {

    return "user/mypage";
  }

  @GetMapping("/users")
  public String createUser() {
    return "user/login/register";
  }

  @PostMapping("/users")
  public String createUser(AccountDto accountDto) {
    ModelMapper modelMapper = new ModelMapper();
    Account account = modelMapper.map(accountDto, Account.class);
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    userService.createUser(account);

    return "redirect:/";
  }
}
