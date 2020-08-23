package io.ssosso.springsecuritypractice1.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfigController {

  @GetMapping(value = "/config")
  public String config(){

    return "admin/config";
  }
}