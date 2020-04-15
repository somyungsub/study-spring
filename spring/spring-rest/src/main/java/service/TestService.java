package service;

import model.Test;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TestService {

  public Test findByName(String name) {
    System.out.println("findByName ");
    String newName = name + "Test ~!! ";
    final Test test = new Test();
    test.setAge(33);
    test.setName(newName);
    return test;
  }

  public List<Test> findAll() {
    System.out.println("findAll call");
    return Arrays.asList(
            new Test("test1", 1),
            new Test("test2", 2),
            new Test("test3", 3),
            new Test("test4", 4)
    );
  }

  public Long create(Test test) {
    System.out.println("Create!!");
    System.out.println("creaet test : " + test);
    return 1L;
  }

  public void update(Test test) {
    System.out.println("Update !!! Create");
  }

  public void delete(String name) {
    System.out.println(" Delete !! Complete");
  }
}

