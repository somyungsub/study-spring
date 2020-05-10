package io.ssosso.jpashop1.domain;

import javax.persistence.*;

@Entity
public class Delivery {
  @Id
  @GeneratedValue
  private Long id;

  @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
  private Order order;

  @Embedded
  private Address address;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus deliveryStatus;

}
