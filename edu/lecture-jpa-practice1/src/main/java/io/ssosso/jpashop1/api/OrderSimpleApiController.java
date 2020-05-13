package io.ssosso.jpashop1.api;

import io.ssosso.jpashop1.domain.Order;
import io.ssosso.jpashop1.domain.OrderSearch;
import io.ssosso.jpashop1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 성능 최적화
 * XToOne 매핑들
 * - 100% 이해하기 실무에서 매우 중요
 * <p>
 * Order
 * Order -> Member    (ManyToOne)
 * Order -> Delivery  (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;

  /**
   * null -> 지연로딩
   *    {
   *         "id": 4,
   *         "member": null,      // 지연로딩
   *         "orderItems": null,  // 지연로딩
   *         "delivery": null,    // 지연로딩
   *         "orderDate": "2020-05-14T04:57:40.015417",
   *         "status": "ORDER",
   *         "totalPrice": 50000
   *     }
   */
  @GetMapping("/api/v1/simple-orders")
  public List<Order> ordersV1() {
    final List<Order> all = orderRepository.findAllByString(new OrderSearch());
    all.stream().forEach(order -> {
      order.getMember().getName();      // 지연로딩 -> 강제 초기화 (select SQL 실행)
      order.getDelivery().getAddress(); // 지연로딩 -> 강제 초기화 (select SQL 실행)
    });
    return all;
  }

}
