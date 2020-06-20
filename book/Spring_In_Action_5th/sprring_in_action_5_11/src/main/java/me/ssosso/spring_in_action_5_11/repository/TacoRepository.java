package me.ssosso.spring_in_action_5_11.repository;

import me.ssosso.spring_in_action_5_11.domain.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoRepository extends ReactiveCrudRepository<Taco, Long> {
}
