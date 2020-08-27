package io.ssosso.springsecuritypractice1.config;

import io.ssosso.springsecuritypractice1.repository.ResourceRepository;
import io.ssosso.springsecuritypractice1.sercurity.service.SecurityResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public SecurityResourceService securityResourceService(ResourceRepository resourceRepository) {
    return new SecurityResourceService(resourceRepository);
  }

}
