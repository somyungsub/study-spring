package com.thoughtmechanix.organization;

import com.thoughtmechanix.organization.events.source.CustomSourceHandler;
import com.thoughtmechanix.organization.utils.UserContextFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;


@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableBinding(Source.class)
public class Org8Application {

  @Bean
  public Filter userContextFilter() {
    UserContextFilter userContextFilter = new UserContextFilter();
    return userContextFilter;
  }

  @Bean
  public CustomSourceHandler customSource() {
    return new CustomSourceHandler();
  }

  public static void main(String[] args) {
    SpringApplication.run(Org8Application.class, args);
  }
}
