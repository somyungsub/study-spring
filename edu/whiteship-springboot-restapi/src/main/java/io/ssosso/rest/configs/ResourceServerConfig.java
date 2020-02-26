package io.ssosso.rest.configs;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;


// 인증이필요-> 토큰, 토큰 유요한지. 이벤트 리소스를 제공하는 서버와 같이 있는 맞고
// 인증서버는 따로 분리하는 것이 맞음
@Configuration
@EnableAuthorizationServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId("event");  // ID 설정, 나머지는 기본으로 남겨뒀음
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .anonymous()
          .and()
        .authorizeRequests()
          .mvcMatchers(HttpMethod.GET, "/api/**")
            .anonymous()
          .anyRequest()
            .authenticated()
          .and()
        .exceptionHandling()
          .accessDeniedHandler(new OAuth2AccessDeniedHandler())
        ;
  }
}
