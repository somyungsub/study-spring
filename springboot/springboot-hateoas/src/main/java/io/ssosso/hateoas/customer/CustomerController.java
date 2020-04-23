package io.ssosso.hateoas.customer;

import io.ssosso.hateoas.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @GetMapping("/{customerId}")
  public Customer getCustomerById(@PathVariable String customerId) {

    return customerService.getCustomerDetail(customerId);
  }
}
