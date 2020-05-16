package io.ssosso.jpashop1.service.query;

import io.ssosso.jpashop1.api.OrderApiController;
import io.ssosso.jpashop1.domain.Order;
import io.ssosso.jpashop1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

  private final OrderRepository orderRepository;

//  public List<OrderApiController.OrderDto> ordersV3() {
//    List<Order> orders = orderRepository.findAllWithItem();
//    final List<OrderApiController.OrderDto> result = orders.stream()
//            .map(OrderApiController.OrderDto::new)
//            .collect(toList());
//
//    return result;
//  }

}
