package me.ssosso.spring_in_action_5_11.controller;

import me.ssosso.spring_in_action_5_11.domain.Taco;
import me.ssosso.spring_in_action_5_11.repository.TacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/design", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {

  private TacoRepository tacoRepository;

  public DesignTacoController(TacoRepository tacoRepository) {
    this.tacoRepository = tacoRepository;
  }

  @GetMapping("/recent")
  public Flux<Taco> recentTacos() {
    return tacoRepository.findAll().take(12);
  }

  @GetMapping("/{id}")
  public Mono<Taco> tacoById(@PathVariable("id") Long id){
    return tacoRepository.findById(id);
  }

  @PostMapping(consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Taco> postTaco(@RequestBody Taco taco) {
    return tacoRepository.save(taco);
  }
//  @PostMapping(consumes = "application/json")
//  @ResponseStatus(HttpStatus.CREATED)
//  public Mono<Taco> postTaco(@RequestBody Mono<Taco> taco) {
//    return tacoRepository.saveAll(taco).next();
//  }
}
