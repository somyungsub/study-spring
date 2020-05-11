package io.ssosso.jpashop1.service;

import io.ssosso.jpashop1.domain.Address;
import io.ssosso.jpashop1.domain.Member;
import io.ssosso.jpashop1.domain.Order;
import io.ssosso.jpashop1.domain.OrderStatus;
import io.ssosso.jpashop1.domain.item.Book;
import io.ssosso.jpashop1.domain.item.Item;
import io.ssosso.jpashop1.exception.NotEnoughStockException;
import io.ssosso.jpashop1.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class OrderServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  OrderService orderService;

  @Autowired
  OrderRepository orderRepository;

  @Test
  @DisplayName("상품주문")
  public void order_item() throws Exception {

    // given
    Member member = createMember();
    em.persist(member);

    Item item = createItem("JPA",10000, 10);
    em.persist(item);

    // when
    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

    // then
    Order order = orderRepository.findOne(orderId);
    assertEquals(OrderStatus.ORDER, order.getStatus(), "상품 주문시 상태는 ORDER");
    assertEquals(1, order.getOrderItems().size(), "주문한 상품 종류수가 정확해야 한다.");
    assertEquals(10000 * orderCount, order.getTotalPrice(),"주문 가격은 가격 * 수량이다.");
    assertEquals(8, item.getStockQuantity(),"주문 수량만큼 재고가 줄어야 한다.");
  }


  @Test
  @DisplayName("상품주문 재고수량 초과")
  public void order_item_stock_proceed() throws Exception {
    // given
    Member member = createMember();
    Item item = createItem("JPA", 10000, 10);
    int orderCount = 11;

    // when & then
    assertThrows(NotEnoughStockException.class, () -> {
      orderService.order(member.getId(), item.getId(), orderCount);
    });

  }

  @Test
  @DisplayName("주문취소")
  public void order_cancel() throws Exception {
    // given
    Member member = createMember();
    final Item item = createItem("JPA", 10000, 10);

    int orderCount = 2;
    final Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

    // when
    orderService.cancelOrder(orderId);

    // then
    final Order find = orderRepository.findOne(orderId);
    assertEquals(OrderStatus.CANCEL, find.getStatus(), "주문취소시 상태는 CANCEL 이다.");
    assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 원복되어야한다 ");

  }

  private Item createItem(String name, int price, int stockQuantity) {
    Item item = new Book();
    item.setName(name);
    item.setPrice(price);
    item.setStockQuantity(stockQuantity);
    em.persist(item);
    return item;
  }

  private Member createMember() {
    Member member = new Member();
    member.setName("회원1");
    member.setAddress(new Address("서울", "강가", "12345"));
    em.persist(member);
    return member;
  }
}