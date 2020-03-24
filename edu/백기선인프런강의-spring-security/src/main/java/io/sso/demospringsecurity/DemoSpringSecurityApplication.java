package io.sso.demospringsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync // 이걸줘야 비동기처리가 가능함, 더 정확히는 쓰레드풀 설정도 필요
public class DemoSpringSecurityApplication {

  @Bean
  public PasswordEncoder passwordEncoder() {
    // password 저장시 인코딩하여 저장 NoOp으로 저장하기
//    return NoOpPasswordEncoder.getInstance();

    /*
      5버전 이후부터 는 NoOp의 디폴트전략이 변경됨, 다양한 인코딩 알고리즘을 사용할 수 있도록
      공식 도큐먼트 참조
        - noop, pdkdf2, scrypt, sha256 ..
        - 디폴트 -> bcrypt
     */
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoSpringSecurityApplication.class, args);
  }

}
