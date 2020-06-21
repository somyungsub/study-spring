package me.ssosso.spring_in_action_5_11.controller;

import me.ssosso.spring_in_action_5_11.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/ingredients", produces="application/json")
@CrossOrigin(origins="*")
public class IngredientController {

  @Autowired
  WebClient webClient;

  @GetMapping
  public Flux<Ingredient> ingredients(@PathVariable String id) {
    Mono<Ingredient> mono = webClient
        .get()
        .uri("/{id}", id)
        .retrieve()
        .bodyToMono(Ingredient.class);

    mono.subscribe(ingredient -> System.out.println("ingredient = " + ingredient));
    return null;
  }


}
