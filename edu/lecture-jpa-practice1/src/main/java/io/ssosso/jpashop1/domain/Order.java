package io.ssosso.jpashop1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // Order 저장시, OrderItem 세팅하면 -> OrderItem 도 같이 저장 하게 끔
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Delivery delivery;


  private LocalDateTime orderDate;    // 주문시간

  @Enumerated(EnumType.STRING)
  private OrderStatus status;   // 주문상태[ORDER, CANCEL]


  // 연관관계 편의 메서드
  public void addMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    this.orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void addDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

}
