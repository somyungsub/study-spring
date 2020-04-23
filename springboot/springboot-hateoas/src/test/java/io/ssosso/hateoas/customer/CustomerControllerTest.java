package io.ssosso.hateoas.customer;

import io.ssosso.hateoas.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
class CustomerControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Test
  public void test() throws Exception {
    final Customer customer
            = Customer.builder()
            .customerName("ssosso!!")
            .companyName("kakaopay")
            .customerId("10A").build();
    mockMvc.perform(get("/customers/" + customer.getCustomerId()))
    .andDo(print())
    ;
    linkTo(CustomerController.class).slash(customer.getCustomerId()).withSelfRel();
  }

}