package me.ssosso.spring_in_action_5_11.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@RestResource(rel = "tacos", path = "tacos")
@NoArgsConstructor
public class Taco {

  @Id
  private String id;

  @NotNull
  @Size(min=5, message="Name must be at least 5 characters long")
  private String name;

  private LocalDateTime createdAt;

//  @Size(min = 1, message = "You must choose at least 1 ingredient")
//  private List<Ingredient> ingredients = new ArrayList<>();

}
