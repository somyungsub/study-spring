package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @Test
  public void test() {
    Item item = new Item("A");
    itemRepository.save(item);  // SimpleJpaRepository 구현체 save 메서드 디버깅으로 확인
  }

}