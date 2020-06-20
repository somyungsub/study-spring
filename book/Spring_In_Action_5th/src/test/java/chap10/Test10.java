package chap10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Test10 {

  private void print(String s) {
    System.out.println(s);
  }

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
  public void create() throws InterruptedException {
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

  @Test
  @DisplayName("mergeWith")
  public void p_353() throws Exception{

    // 시간순으로 방출
    Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
        .delayElements(Duration.ofMillis(500));

    Flux<String> foodFlux = Flux.just("Lasagna", "Lollipos", "Apples")
        .delaySubscription(Duration.ofMillis(700L))
        .delayElements(Duration.ofMillis(500));

    Flux<String> result = characterFlux.mergeWith(foodFlux);
    result.subscribe(this::print);

    Thread.sleep(3000L);
  }

  @Test
  @DisplayName("zip")
  public void p_354() throws Exception{
    // 한번씩 번갈아가면서 방출 , 순서유지
    Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
        .delayElements(Duration.ofMillis(500));

    Flux<String> foodFlux = Flux.just("Lasagna", "Lollipos", "Apples")
        .delaySubscription(Duration.ofMillis(700L))
        .delayElements(Duration.ofMillis(500));

    Flux<Tuple2<String, String>> result = Flux.zip(characterFlux, foodFlux);
    result.subscribe(objects -> System.out.println(objects.getT1() + ", " + objects.getT2()));

    Thread.sleep(3000L);
  }

  @Test
  @DisplayName("zip2")
  public void p_354_2() throws Exception{

    Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
        .delayElements(Duration.ofMillis(500));

    Flux<String> foodFlux = Flux.just("Lasagna", "Lollipos", "Apples")
        .delaySubscription(Duration.ofMillis(700L))
        .delayElements(Duration.ofMillis(500));

    // 반환값 변경 (map과 유사)
    Flux<String> result = Flux.zip(characterFlux, foodFlux, (s, s2) -> s + " eats " + s2);
    result.subscribe(this::print);

    Thread.sleep(3000L);
  }

  @Test
  @DisplayName("first")
  public void p_356() throws Exception{
    // 둘중에 먼저 통지 되는 flux를 선택하여 통지
    Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
        .delaySubscription(Duration.ofMillis(100));
    Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

    Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);

    firstFlux.subscribe(this::print);
  }



}
