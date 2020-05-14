package io.ssosso.jpashop1.api;

import io.ssosso.jpashop1.domain.Order;
import io.ssosso.jpashop1.domain.OrderItem;
import io.ssosso.jpashop1.domain.OrderSearch;
import io.ssosso.jpashop1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
