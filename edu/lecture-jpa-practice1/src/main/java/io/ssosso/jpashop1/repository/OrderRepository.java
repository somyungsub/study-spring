package io.ssosso.jpashop1.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ssosso.jpashop1.domain.Order;
import io.ssosso.jpashop1.domain.OrderSearch;
import io.ssosso.jpashop1.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static io.ssosso.jpashop1.domain.QMember.member;
import static io.ssosso.jpashop1.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager em;

  public void save(Order order) {
    em.persist(order);
  }

  public Order findOne(Long id) {
    return em.find(Order.class, id);
  }

//  public List<Order> findAll(OrderSearch orderSearch) {
//
//    // 동적쿼리 -> QueryDSL 강추, Criteria 비추
//
//
//    return em.createQuery("select o from Order o join o.member m " +
//            "where o.status =: status " +
//            "and m.name like :name", Order.class)
//            .setParameter("status", orderSearch.getOrderStatus())
//            .setParameter("name", orderSearch.getMemberName())
//            .setMaxResults(1000)  // 최대 1000건
//            .getResultList();
//  }

  public List<Order> findAllByString(OrderSearch orderSearch) {
    //language=JPAQL
    String jpql = "select o From Order o join o.member m";
    boolean isFirstCondition = true;     //주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " o.status = :status";
    }     //회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " m.name like :name";
    }
    TypedQuery<Order> query = em.createQuery(jpql, Order.class)
            .setMaxResults(1000); //최대 1000건
    if (orderSearch.getOrderStatus() != null) {
      query = query.setParameter("status", orderSearch.getOrderStatus());
    }
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      query = query.setParameter("name", orderSearch.getMemberName());
    }
    return query.getResultList();
  }

  public List<Order> findAll(OrderSearch orderSearch) {

    JPAQueryFactory query = new JPAQueryFactory(em);

    // QueryDsl -> JPQL 로 변환되어서 실행되며, 코드 가독성이 현저히 올라감
    return query
            .select(order)
            .from(order)
            .join(order.member, member)
            .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
//            .where(order.status.eq(orderSearch.getOrderStatus()))
            .limit(1000)
            .fetch();
  }

  private BooleanExpression nameLike(String memberName) {
    if (StringUtils.hasText(memberName)) {
      return  null;
    }
    return member.name.like(memberName);
  }

  private BooleanExpression statusEq(OrderStatus orderStatus) {
    if (orderStatus == null) {
      return null;
    }
    return order.status.eq(orderStatus);
  }

  public List<Order> findAllWithMemberDelivery() {

    // Lazy 무시, 모든 연관데이터 로딩하여 다 가지고옴
    return em.createQuery(
            "select o from Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d", Order.class
    ).getResultList();
  }

  public List<Order> findAllWithItem() {
    /*
      일대다 주의사항 (OneToMany)
      페이징 불가 - 반드시 알아둘 것!!
      메모리에 올랄감 -> 조심
     */
    return em.createQuery(
            "select distinct o from Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d " +
                    "join fetch o.orderItems oi " +
                    "join fetch oi.item i ", Order.class)
            .setFirstResult(1)
            .setMaxResults(100)
            .getResultList();
  }

  public List<Order> findAllWithMemberDelivery(int offset, int limit) {
    return em.createQuery(
            "select o from Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d", Order.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();

  }

//  public List<OrderSimpleQueryDto> findOrderDtos() {
//    return em.createQuery(
//            "select new io.ssosso.jpashop1.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
//                    "from Order o " +
//            "join o.member m " +
//            "join o.delivery d", OrderSimpleQueryDto.class)
//            .getResultList();
//  }
}
