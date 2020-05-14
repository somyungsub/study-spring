package io.ssosso.jpashop1.api;

import io.ssosso.jpashop1.domain.*;
import io.ssosso.jpashop1.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;

  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearch());

    // 지연로딩 초기화
    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();
      List<OrderItem> orderItems = order.getOrderItems();
      orderItems.forEach(o -> o.getItem().getName());
    }

    return  all;
  }

  @GetMapping("/api/v2/orders")
  public List<OrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());
    final List<OrderDto> result = orders.stream()
            .map(OrderDto::new)
            .collect(toList());

    return result;
  }

  @Getter
  static class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
//    private List<OrderItem> orderItems; // 엔티티 -> api 스펙에서 제외시켜야 변경에 유연
    private List<OrderItemDto> orderItems; // Dto 를 따로 만들어서 제공할 것

    public OrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getMember().getAddress();
//      order.getOrderItems().stream().forEach(o -> o.getItem().getName());
//      orderItems = order.getOrderItems();
      orderItems = order.getOrderItems().stream()
              .map(OrderItemDto::new)
              .collect(toList());
    }
  }

  @Getter
  static class OrderItemDto {

    private String itemName;    // 상품명
    private int orderPrice;     // 주문 가격
    private int count;          // 주문 수량

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }
  }

}
