package com.example.demo.login.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

  @GetMapping(value = {"/", "/login"})
  public String entry() {
    System.out.println("test");
    return "index";
  }

  @PostMapping(value = "/api/testPost")
  public ResponseEntity<Map<String, Object>> testPost(@RequestBody Map<String, Object> request) {
    System.out.println(request);
    Map<String, Object> res = new HashMap<>();
    res.put("success", true);
    return ResponseEntity.ok(res);
  }

  @PostMapping(value = "/testPost")
  public ResponseEntity<Map<String, Object>> testPost2(@RequestBody Map<String, Object> request) {
    System.out.println(request);
    Map<String, Object> res = new HashMap<>();
    res.put("success", true);
    res.put("test2", true);
    return ResponseEntity.ok(res);
  }

}
