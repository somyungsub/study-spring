package io.ssosso.springdatajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Member {

  @Id @GeneratedValue
  private Long id;
  private String username;

  // JPA -> 기본적으로 ,디폴트 생성자 필수 -> Proxy 기술을 사용할 때 필요하게 됨. private는 안됨 그래서
  protected Member() {
  }

  public Member(String username) {
    this.username = username;
  }

}
