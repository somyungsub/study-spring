package io.ssosso.jpashop1.repository.order.query;

import io.ssosso.jpashop1.domain.Address;
import io.ssosso.jpashop1.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

// sql 형식의
@Data
public class OrderFlatDto {
  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;

  private String itemName;
  private int orderPrice;
  private int count;

  public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
    this.itemName = itemName;
    this.orderPrice = orderPrice;
    this.count = count;
  }
}
