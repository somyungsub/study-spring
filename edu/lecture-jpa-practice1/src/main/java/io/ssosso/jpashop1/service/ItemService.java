package io.ssosso.jpashop1.service;

import io.ssosso.jpashop1.domain.item.Book;
import io.ssosso.jpashop1.domain.item.Item;
import io.ssosso.jpashop1.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
  private final ItemRepository itemRepository;

  @Transactional
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  @Transactional
  public Item updateItem(Long itemId, Book param) {
    // 1. 변경감지 기능 사용, merge는 비추 !! . null 이런 값들이 업데이트 될 수 있음
    Item findItem = itemRepository.findOne(itemId); // 조회를 해서 처리해야함. 영속상태에서

    // set 대신 .. 편의메서드로 만들어야 변경지점이 다 전딜될 수 있게 설계해야함
    findItem.setPrice(param.getPrice());
    findItem.setName(param.getName());
    findItem.setStockQuantity(param.getStockQuantity());
    return findItem;
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long itemId) {
    return itemRepository.findOne(itemId);
  }

  @Transactional
  public void updateItem(long itemId, String name, int price, int stockQuantity) {
    Item findItem = itemRepository.findOne(itemId);
    findItem.setPrice(price);
    findItem.setName(name);
    findItem.setStockQuantity(stockQuantity);
  }
}
