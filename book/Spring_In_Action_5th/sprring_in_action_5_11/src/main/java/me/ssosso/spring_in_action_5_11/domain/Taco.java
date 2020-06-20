package me.ssosso.spring_in_action_5_11.domain;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Taco {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Size(min=5, message="Name must be at least 5 characters long")
  private String name;

  private LocalDateTime createdAt;

//  @ManyToMany(targetEntity=Ingredient.class)
//  @Size(min=1, message="You must choose at least 1 ingredient")
//  private List<Ingredient> ingredients;

  @PrePersist
  void createdAt() {
    this.createdAt = LocalDateTime.now();
  }
}
