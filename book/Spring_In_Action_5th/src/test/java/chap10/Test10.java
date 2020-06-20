package chap10;

import common.DebugSubscriber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Test10 {

  private Flux<String> fluxJust() {
    return Flux.just("A", "B", "C", "D", "E", "F");
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
    result.subscribe(new DebugSubscriber<>());

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
    result.subscribe(new DebugSubscriber<>());

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

    firstFlux.subscribe(new DebugSubscriber<>());
  }

  @Test
  @DisplayName("skip")
  public void p_357() throws Exception{

    // 해당시간만큼 지난 후 통지
    Flux<String> skipFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
        .skip(3);
    skipFlux.subscribe(new DebugSubscriber<>());
  }

  @Test
  @DisplayName("skip-delay")
  public void p_358() throws Exception{

    // 해당 시간만큼 지난 후 통지 + 통지 지연시간 추가
    Flux<String> skipFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
        .delayElements(Duration.ofSeconds(1))
        .skip(Duration.ofSeconds(3));

    skipFlux.subscribe(new DebugSubscriber<>());
    Thread.sleep(5500L);
  }
  
  @Test
  @DisplayName("take")
  public void p_359() throws Exception{

    // skip 과 반대 -> 해당 시간만큼 데이터만 통지
    Flux<String> flux = fluxJust()
        .delayElements(Duration.ofSeconds(1))
        .take(Duration.ofMillis(3500));

    flux.subscribe(new DebugSubscriber<>());
    Thread.sleep(4000L);

  }

  @Test
  @DisplayName("filter")
  public void p_360() throws Exception{
    // 조건식에 맞는 것만
    Flux<String> flux = fluxJust().filter(s -> !s.contains("C"));
    flux.subscribe(new DebugSubscriber<>());
    Thread.sleep(1000L);
  }
  
  @Test
  @DisplayName("distinct")
  public void p_361() throws Exception{

    // 중복제거
    Flux<String> flux = Flux.just("A", "B", "A", "C", "D", "C").distinct();
    flux.subscribe(new DebugSubscriber<>());
  }

  @Test
  @DisplayName("map")
  public void p_362() throws Exception{
    // map -> 동기, 순차적으로 처리 됨
    Flux<String> flux = fluxJust().map(s -> s + "!!!");
    flux.subscribe(new DebugSubscriber<>());
  }

  @Test
  @DisplayName("flayMap")
  public void p_363() throws Exception{
    // 비동기적으로 처리
    Flux<String> flux = fluxJust()
        .flatMap(
            s -> Flux.just(s).map(s1 -> s1 + "!!!!!").subscribeOn(Schedulers.parallel()
            )
        );
    flux.subscribe(new DebugSubscriber<>());
  }

  @Test
  @DisplayName("buffer")
  public void p_364() throws Exception{
    // 버퍼사이즈만큼 통지 -> 비효율적으로 보임
    Flux<List<String>> flux = fluxJust().buffer(4);
    flux.subscribe(new DebugSubscriber<>());
  }

  @Test
  @DisplayName("buffer + flatMap 조합")
  public void p_365() throws Exception{
    fluxJust()
        .buffer(4)
        .flatMap(strings -> Flux.fromIterable(strings)
            .map(s -> s.toUpperCase())
            .subscribeOn(Schedulers.parallel())
            .log()
        ).subscribe(new DebugSubscriber<>());

  }

  @Test
  @DisplayName("collectList")
  public void p_366() throws Exception{

    Mono<List<String>> mono = fluxJust().collectList();
    mono.subscribe(new DebugSubscriber<>());

    Mono<Map<String, String>> mapMono =
        fluxJust().collectMap(s -> s.charAt(0) + "!!");
    mapMono.subscribe(new DebugSubscriber<>());

  }



}
