package io.ssosso.rest.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id") // 해쉬코드 생성
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

  @Id
  @GeneratedValue
  private Integer id;
  private String email;
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(value = EnumType.STRING)
  private Set<AccountRole> roles;
}
