package io.ssosso.jpashop1.repository.order.query;

import io.ssosso.jpashop1.domain.Address;
import io.ssosso.jpashop1.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderQueryDto {
  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;
  private List<OrderItemQueryDto> oorderItems;

  public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
//    this.orderItemQueryDtos = orderItemQueryDtos;
  }
}
