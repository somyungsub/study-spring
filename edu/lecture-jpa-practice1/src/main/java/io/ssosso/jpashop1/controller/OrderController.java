package io.ssosso.jpashop1.controller;

import io.ssosso.jpashop1.domain.Member;
import io.ssosso.jpashop1.domain.Order;
import io.ssosso.jpashop1.domain.OrderSearch;
import io.ssosso.jpashop1.domain.item.Item;
import io.ssosso.jpashop1.service.ItemService;
import io.ssosso.jpashop1.service.MemberService;
import io.ssosso.jpashop1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final MemberService memberService;
  private final ItemService itemService;

  @GetMapping("/order")
  public String createForm(Model model) {
    List<Member> members = memberService.findMembers();
    List<Item> items = itemService.findItems();

    model.addAttribute("members", members);
    model.addAttribute("items", items);
    return "order/orderForm";
  }

  @PostMapping("/order")
  public String order(@RequestParam Long memberId,
                      @RequestParam Long itemId,
                      @RequestParam int count) {

    orderService.order(memberId, itemId, count);
    return "redirect:/orders";
  }

  @GetMapping("orders")
  public String orderList(@ModelAttribute OrderSearch orderSearch, Model model) {
    final List<Order> orders = orderService.findOrders(orderSearch);
    model.addAttribute("orders", orders);
    return "order/orderList";
  }

  @PostMapping("/orders/{orderId}/cancel")
  public String cancelOrder(@PathVariable Long orderId) {
    orderService.cancelOrder(orderId);

    return "redirect:/orders";
  }
}
