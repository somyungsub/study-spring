package io.ssosso.jpashop1.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderItemQueryDto {

  @JsonIgnore
  private Long orderId; // 꼭 없어도 되긴함

  private String itemName;
  private int orderPrice;
  private int count;

  public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
    this.orderId = orderId;
    this.itemName = itemName;
    this.orderPrice = orderPrice;
    this.count = count;
  }
}
