package io.ssosso.jpashop1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserTestTest {

  @Autowired
  EntityManager em;

  @Test
  @DisplayName("프로그래머스")
  public void test(){
    System.out.println("em = " + em);
//
//    UserTest userTest = new UserTest();
//    userTest.setEmail("a@b");
//    userTest.setName("ss");
//    userTest.setProfileImageUrl("asda");
//    userTest.setPasswd("1111");
//
//    em.persist(userTest);
//
//    UserTest test = em.find(UserTest.class, 1L);
//
//    System.out.println("test = " + test);



  }

}