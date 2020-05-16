package io.ssosso.jpashop1.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
  private final EntityManager em;

  public List<OrderQueryDto> findOrderQueryDtos() {

    final List<OrderQueryDto> orders = findOrders();  // 1번 -> N개

    // OrderItems 따로 채우기
    orders.forEach(o -> {
      List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // N개
      o.setOrderItems(orderItems);
    });

    return orders;
  }

  // v4 쿼리 최적화
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

  // v5 컬렉션 조회 최적화
  public List<OrderQueryDto> findAllByDto_optimization() {

    List<OrderQueryDto> orders = findOrders();
    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(orders));

    orders.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));

    return orders;
  }

  private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
    List<OrderItemQueryDto> orderItems = em.createQuery(
            "select new io.ssosso.jpashop1.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                    "from OrderItem oi " +
                    "join oi.item i " +
                    "where oi.order.id in :orderIds"
            , OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();

    return orderItems.stream()
            .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
  }

  private List<Long> toOrderIds(List<OrderQueryDto> orders) {
    return orders.stream()
              .map(OrderQueryDto::getOrderId)
              .collect(Collectors.toList());
  }

  // v6
  public List<OrderFlatDto> findAllByDto_flat() {
    return em.createQuery(
            "select new io.ssosso.jpashop1.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status,d.address,i.name,oi.orderPrice,oi.count)" +
                    "from Order o " +
                    "join o.member m " +
                    "join o.delivery d " +
                    "join o.orderItems oi " +
                    "join oi.item i", OrderFlatDto.class
    ).getResultList();
  }
}
