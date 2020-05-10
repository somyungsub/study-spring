package io.ssosso.jpashop1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

  private String city;
  private String street;
  private String zipcode;

  // JPA 스펙상 디폴트 생성자 필요.. -> protected 까지 허용해주므로, public 대신 protected 로 해두자!
  protected Address() {
  }

  // 불변 값을 위해 setter 제거하고, 생성자로 한번만 초기화 할 수 있도록 설계
  public Address(String city, String street, String zipcode) {
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
  }
}
