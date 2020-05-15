package io.ssosso.jpashop1.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
  private final EntityManager em;

  public List<OrderQueryDto> findOrderQueryDtos() {

    final List<OrderQueryDto> orders = findOrders();  // 1번 -> N개

    // OrderItems 따로 채우기
    orders.forEach(o -> {
      List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // N개
      o.setOorderItems(orderItems);
    });

    return orders;
  }

  private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery(
            "select new io.ssosso.jpashop1.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
            "from OrderItem oi " +
            "join oi.item i " +
            "where oi.order.id =: orderId", OrderItemQueryDto.class)
            .setParameter("orderId", orderId)
            .getResultList();
  }

  private List<OrderQueryDto> findOrders() {
    return em.createQuery(
            "select new io.ssosso.jpashop1.repository.order.query.OrderQueryDto(o.id, m.name,o.orderDate,o.status,d.address) " +
                    "from Order o " +
                    "join o.member m " +
                    "join o.delivery d", OrderQueryDto.class).getResultList();
  }

}
