package io.ssosso.jpashop1.domain;

import io.ssosso.jpashop1.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  @Id
  @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  private int orderPrice;   // 주문가격
  private int count;        // 주문수량

  // == 생성 메서드 ==//
  public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setOrderPrice(orderPrice);
    orderItem.setCount(count);

    item.removeStock(count);

    return orderItem;
  }

  // == 비즈니스 로직 ==//
  public void cancel() {
    getItem().addStock(count);  // 재고수량 원복
  }

  // == 조회 로직 ==//
  /**
   * 주문상품 전체 가격 조회
   */
  public int getTotalPrice() {
    return getCount() * getOrderPrice();
  }
}
