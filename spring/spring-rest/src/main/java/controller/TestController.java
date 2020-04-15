package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

  @RequestMapping(value = "/{id}", method = RequestMethod.POST)
  public String getId(@PathVariable("id") String id) {
    System.out.println("id = " + id);
    return "sample";
  }

  @RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
  public String sample(@PathVariable String id) {
    System.out.println("sample : " + id);
    return "sample";
  }

  @RequestMapping(value = "/v2/{id}", method = RequestMethod.GET)
  public String test(@PathVariable String id) {
    System.out.println("test2 : " + id);
    return "test";
  }
}
