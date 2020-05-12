package io.ssosso.jpashop1;

import io.ssosso.jpashop1.domain.*;
import io.ssosso.jpashop1.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


/**
 * 총 주문 2개
 * UserA
 * - JPA1 Book
 * - JPA2 Book
 * UserB
 * - Spring1 Book
 * - Spring2 Book
 */
@Component
@RequiredArgsConstructor
public class InitDb {

  private final InitService initService;

  @PostConstruct
  public void init() {  // 초기화 메서드
    initService.dbInit1();
    initService.dbInit2();
  }

  @Component
  @Transactional
  @RequiredArgsConstructor
  static class InitService {

    private final EntityManager em;

    public void dbInit1() {
      Member member = createMember("userA", "서울", "1", "1111");
      em.persist(member);

      Book book = createBook("JAP1 Book", 10000, 100);
      em.persist(book);

      Book book2 = createBook("JAP2 Book", 20000, 100);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

      Delivery delivery = createDelivery(member);

      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);

    }

    public void dbInit2() {
      Member member = createMember("userB", "전주", "1ㅈ", "1111");
      em.persist(member);

      Book book = createBook("Spring1 Book", 10000, 200);
      em.persist(book);

      Book book2 = createBook("Spring2 Book", 20000, 300);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book, 20000, 3);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

      Delivery delivery = createDelivery(member);

      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);

    }

    private Delivery createDelivery(Member member) {
      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      return delivery;
    }

    private Book createBook(String name, int price, int stockQuantity) {
      Book book = new Book();
      book.setName(name);
      book.setPrice(price);
      book.setStockQuantity(stockQuantity);
      return book;
    }

    private Member createMember(String name, String city, String street, String zipcode) {
      Member member = new Member();
      member.setName(name);
      member.setAddress(new Address(city, street, zipcode));
      return member;
    }
  }
}


