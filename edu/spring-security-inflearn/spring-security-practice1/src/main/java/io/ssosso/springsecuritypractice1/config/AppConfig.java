package io.ssosso.springsecuritypractice1.config;

import io.ssosso.springsecuritypractice1.repository.ResourcesRepository;
import io.ssosso.springsecuritypractice1.sercurity.service.SecurityResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public SecurityResourceService securityResourceService(ResourcesRepository resourcesRepository) {
    return new SecurityResourceService(resourcesRepository);
  }

}
