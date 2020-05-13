package io.ssosso.jpashop1;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Jpashop1Application {

  public static void main(String[] args) {
    SpringApplication.run(Jpashop1Application.class, args);
  }

  @Bean
  public Hibernate5Module hibernate5Module() {
    final Hibernate5Module hibernate5Module = new Hibernate5Module();
    /*
        지연로딩 -> 강제로 로딩 -> 강제로 로딩
        절대 쓰지 말것
        - Entity 를 외부로 노출 하지 말것
     */
//    hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
    return hibernate5Module;
  }


}
