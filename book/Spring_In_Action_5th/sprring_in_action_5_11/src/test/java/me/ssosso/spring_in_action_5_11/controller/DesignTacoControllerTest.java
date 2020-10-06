package me.ssosso.spring_in_action_5_11.controller;

import me.ssosso.spring_in_action_5_11.domain.Ingredient;
import me.ssosso.spring_in_action_5_11.domain.Taco;
import me.ssosso.spring_in_action_5_11.repository.TacoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DesignTacoControllerTest {

  @Test
  @DisplayName("GET요청")
  public void shouldReturnRecentTacos() {
    Taco[] tacos = new Taco[16];
    for (int i = 0; i < tacos.length; i++) {
      tacos[i] = testTaco((long) i);
    }

    Flux<Taco> tacoFlux = Flux.just(tacos);
    TacoRepository tacoRepoMock = mock(TacoRepository.class);
    when(tacoRepoMock.findAll()).thenReturn(tacoFlux);

    WebTestClient testClient = WebTestClient.bindToController(new DesignTacoController(tacoRepoMock)).build();

    testClient.get().uri("/design/recent")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$").isArray()
      .jsonPath("$").isNotEmpty()
      .jsonPath("$[0].id").isEqualTo(tacos[0].getId())
      .jsonPath("$[0].name").isEqualTo(tacos[0].getName())
      .jsonPath("$[1].id").isEqualTo(tacos[1].getId())
      .jsonPath("$[1].name").isEqualTo(tacos[1].getName())
      .jsonPath("$[11].id").isEqualTo(tacos[11].getId())
      .jsonPath("$[11].name").isEqualTo(tacos[11].getName())
      .jsonPath("$[12]").doesNotExist()
    ;

  }

  private Taco testTaco(long i) {
    List<Ingredient> list = new ArrayList<>();
    list.add(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
    list.add(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));

    return Taco.builder()
      .id(UUID.randomUUID().toString())
      .name("Taco-" + i)
//      .ingredients(list)
      .build();
  }

}