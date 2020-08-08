package io.ssosso.jpashop1.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users",  uniqueConstraints = @UniqueConstraint(name = "unq_user_email", columnNames = {"email"}))
public class UserTest {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Column(nullable = false, length = 10)
  private String name;

  @Column(nullable = false, length = 50)
  private String email;

  @Column(nullable = false, length = 80)
  private String passwd;

  @Column
  @ColumnDefault("null")
  private String profileImageUrl;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer loginCount;

  @Column(columnDefinition = "datetime not null")
  private LocalDateTime lastLoginAt;

  @Column(columnDefinition = "datetime not null default current_timestamp()")
  private LocalDateTime createAt;

}
