package io.ssosso.jpashop1.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

  private final EntityManager em;


  /**
   * 쿼리 전용 레포지토리를 만들어서 관리 한다
   */
  public List<OrderSimpleQueryDto> findOrderDtos() {
    return em.createQuery(
            "select new io.ssosso.jpashop1.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                    "from Order o " +
            "join o.member m " +
            "join o.delivery d", OrderSimpleQueryDto.class)
            .getResultList();
  }
}
