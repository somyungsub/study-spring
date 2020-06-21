package me.ssosso.spring_in_action_5_11.domain;

import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.rest.core.annotation.RestResource;

@Data
@Entity
@Builder
@RestResource(rel = "tacos", path = "tacos")
public class Taco {

  @Id
  private String id;

  @NotNull
  @Size(min=5, message="Name must be at least 5 characters long")
  private String name;

  private LocalDateTime createdAt;

  @Size(min = 1, message = "You must choose at least 1 ingredient")
  private List<Ingredient> ingredients = new ArrayList<>();

}
