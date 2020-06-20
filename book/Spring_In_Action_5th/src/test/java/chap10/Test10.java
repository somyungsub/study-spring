package chap10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Test10 {
  @Test
  @DisplayName("just")
  public void p_348(){
    Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");

    fruitFlux.subscribe(f -> System.out.println("fruit : " + f));

    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .expectNext("Strawberry")
        .verifyComplete();
  }

  @Test
  @DisplayName("fromArray")
  public void p_350(){
    String[] fruits = {"Apple", "Orange", "Grape", "Banana", "Strawberry"};
    Flux<String> fruitFlux = Flux.fromArray(fruits);

    fruitFlux.subscribe(f -> System.out.println("fruit : " + f));

    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .expectNext("Strawberry")
        .verifyComplete();
  }

  @Test
  @DisplayName("fromIterable")
  public void p_351(){
    String[] fruits = {"Apple", "Orange", "Grape", "Banana", "Strawberry"};
    List<String> fruitList = Arrays.asList(fruits);
    Flux<String> fruitFlux = Flux.fromIterable(fruitList);

    fruitFlux.subscribe(f -> System.out.println("fruit : " + f));

    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .expectNext("Strawberry")
        .verifyComplete();
  }

  @Test
  @DisplayName("fromStream")
  public void p_351_2(){
    String[] fruits = {"Apple", "Orange", "Grape", "Banana", "Strawberry"};
    Stream<String> stream = Arrays.stream(fruits);
    Flux<String> fruitFlux = Flux.fromStream(stream);

    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .expectNext("Strawberry")
        .verifyComplete();
  }

  @Test
  @DisplayName("range")
  public void p_351_3() {
    Flux<Integer> range = Flux.range(1, 5);
    range.subscribe(f -> System.out.println("fruit : " + f));

  }
  
  @Test
  @DisplayName("interval")
  public void p_352() throws InterruptedException {
    Flux<Long> flux = Flux.interval(Duration.ofMillis(100L))
        .take(5);
    flux.subscribe(System.out::println);

    Thread.sleep(1000L);
  }

  @Test
  @DisplayName("create")
  public void create  () throws InterruptedException {
    Flux<Object> flux = Flux.create(fluxSink -> {
      fluxSink.next(1L);
      fluxSink.next(2L);
      fluxSink.next(3L);
      fluxSink.complete();
    }, FluxSink.OverflowStrategy.BUFFER)
        .doOnComplete(() -> System.out.println("완료!!"));

    flux.subscribe(f -> System.out.println("sub : " + f));

    Thread.sleep(1000L);

  }

  


}
