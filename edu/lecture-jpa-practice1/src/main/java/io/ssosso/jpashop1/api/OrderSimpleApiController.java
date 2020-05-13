package io.ssosso.jpashop1.api;

import io.ssosso.jpashop1.domain.Address;
import io.ssosso.jpashop1.domain.Order;
import io.ssosso.jpashop1.domain.OrderSearch;
import io.ssosso.jpashop1.domain.OrderStatus;
import io.ssosso.jpashop1.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

  /**
   * Order -> SQL 1 번
   * Order.Member -> SQL 멤버 수, 단건조회       (ex 2번)
   * Order.Delivery -> SQL 딜리버리 수, 단건조회  (ex 2번)
   * -> 총 5번 sql 실행됨... [N+1 문제] -> 1 + 2 + 2
   *  -> 1 : orders 실행
   *  -> N : 연관된 객체의 sql 실행 (지연로딩에 의해)
   *       : N은 연관된 인스턴스 수만큼 select sql 실행됨 (단건으로 실행되서 그럼..)
   */
  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> ordersV2() {
    final List<Order> orders = orderRepository.findAllByString(new OrderSearch());
    final List<SimpleOrderDto> collect = orders.stream()
            .map(SimpleOrderDto::new)
            .collect(Collectors.toList());

    return collect;
  }

  @Data
  static class SimpleOrderDto{
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();           // Lazy 초기화 시점1
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();   // Lazy 초기화 시점2
    }
  }

}
