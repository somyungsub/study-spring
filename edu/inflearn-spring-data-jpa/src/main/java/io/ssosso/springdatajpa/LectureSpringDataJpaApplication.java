package io.ssosso.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing      // @CreatedDate,By @LastModifyDate,By  사용 하기위한 설정
public class LectureSpringDataJpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(LectureSpringDataJpaApplication.class, args);
  }

  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> Optional.of(UUID.randomUUID().toString());
  }

}
