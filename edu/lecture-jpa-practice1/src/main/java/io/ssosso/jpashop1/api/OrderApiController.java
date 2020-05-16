package io.ssosso.jpashop1.api;

import io.ssosso.jpashop1.domain.*;
import io.ssosso.jpashop1.repository.OrderRepository;
import io.ssosso.jpashop1.repository.order.query.OrderFlatDto;
import io.ssosso.jpashop1.repository.order.query.OrderItemQueryDto;
import io.ssosso.jpashop1.repository.order.query.OrderQueryDto;
import io.ssosso.jpashop1.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

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

  @GetMapping("/api/v3/orders")
  public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();
    final List<OrderDto> result = orders.stream()
            .map(OrderDto::new)
            .collect(toList());

    return result;
  }

  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                      @RequestParam(value = "limit", defaultValue = "100") int limit) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
    final List<OrderDto> result = orders.stream()
            .map(OrderDto::new)
            .collect(toList());

    return result;
  }

  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderQueryDtos();
  }

  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> ordersV5() {
    return orderQueryRepository.findAllByDto_optimization();
  }

  @GetMapping("/api/v6/orders")
  public List<OrderQueryDto> ordersV6() {
    final List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

    // sql -> 중복되는 부분(필드상)을 제거 [4건 -> 2건] , OrderItem 처럼 컬렉션에 의해 row중복을 최적화
    // Order 기준의 페이징은 안됨, OrderItem 기준으로 페이징을 해야함 -> 컬렉션에 따른 row
    return flats.stream()
            .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                    mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getName(), o.getOrderPrice(), o.getCount()), toList())
                    )).entrySet().stream()
            .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
            .collect(toList());
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
