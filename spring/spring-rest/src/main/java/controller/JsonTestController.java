package controller;

import com.TestUtils;
import model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import service.TestService;

import java.util.List;

@RestController
@RequestMapping("/json")
public class JsonTestController {

  @Autowired
  TestService testService;

  @GetMapping
  public List<Test> findAll() {
    return testService.findAll();
  }

  @GetMapping("/{name}")
  public @ResponseBody Test findByName(@PathVariable String name) {
    return testService.findByName(name);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody Test test) {
    return testService.create(test);
  }

  @PutMapping("/{name}")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody void update(@PathVariable String name, @RequestBody Test test) {
    TestUtils.check(testService.findByName(name));
    testService.update(test);
  }

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.OK)
  public void delete(@PathVariable String name) {
    testService.delete(name);
  }
}
