package io.sssosso.restfulwebservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

  // GET
  // /hello-world (endpoint)
  // @RequestMapping(path="/hello-world", method=RequestMethod.GET)
  @GetMapping("/hello-world")
  public String helloWorld(){
    return "Hello World";
  }

  @GetMapping("/hello-world-bean")
  public HelloWorldBean helloWorldBean(){
    return new HelloWorldBean("Hello World");
  }


}
