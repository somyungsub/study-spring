package io.ssosso.rest.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

/*
  문자열을 외부 설정 하기
 */
@Component
@ConfigurationProperties(prefix = "my-app")
@Getter @Setter
public class AppProperties {

  @NotEmpty
  private String adminUsername;

  @NotEmpty
  private String adminPassword;

  @NotEmpty
  private String userPassword;

  @NotEmpty
  private String userUsername;

  @NotEmpty
  private String clientId;

  @NotEmpty
  private String clientSecret;



}
