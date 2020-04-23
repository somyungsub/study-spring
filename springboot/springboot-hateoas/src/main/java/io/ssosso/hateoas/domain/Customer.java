package io.ssosso.hateoas.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@Setter
@Getter
@Builder
public class Customer extends WebMvcLinkBuilder {
  private String customerId;
  private String customerName;
  private String companyName;

}
