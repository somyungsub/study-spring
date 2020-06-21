package me.ssosso.spring_in_action_5_11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SpringInAction511Application {

  public static void main(String[] args) {
    SpringApplication.run(SpringInAction511Application.class, args);
  }

  @Bean
  public WebClient webClient() {
    return WebClient.create("http://localhost:8080");
  }

}
