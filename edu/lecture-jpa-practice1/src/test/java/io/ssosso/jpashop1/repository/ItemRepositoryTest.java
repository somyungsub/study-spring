package io.ssosso.jpashop1.repository;

import io.ssosso.jpashop1.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ItemRepositoryTest {

  @Autowired
  EntityManager em;

  @Test
  public void test() throws Exception {
    Book book = em.find(Book.class, 1L);

    // TX 변경감지 -> 더티체킹 -> Update 실행
    book.setName("aasd");

  }

}