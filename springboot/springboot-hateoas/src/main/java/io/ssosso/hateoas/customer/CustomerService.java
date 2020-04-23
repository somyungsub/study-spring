package io.ssosso.hateoas.customer;

import io.ssosso.hateoas.domain.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
  public Customer getCustomerDetail(String customerId) {
    return Customer.builder().customerId(customerId).companyName("kakaopay").customerName("ssosso").build();
  }
}
