package com.thoughtmechanix.licenses;

import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.utils.UserContextInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableResourceServer
public class License7Application {

    @Autowired
    private ServiceConfig serviceConfig;

    private static final Logger logger = LoggerFactory.getLogger(License7Application.class);

    // @Bean
    // public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
    //                                              OAuth2ProtectedResourceDetails details) {
    //     return new OAuth2RestTemplate(details, oauth2ClientContext);
    // }

    /*
       Bean 등록 -> 호출방식 변경 (Authorization 헤더 정보 : 토큰값) 유입 필터 구현을 안해도 되게 해줌
       - 동작 방식 -> com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient 에서 확인 가능
       - OAuth만 사용할때
     */
//    @Bean
//    public OAuth2RestTemplate restTemplate(UserInfoRestTemplateFactory factory) {
//        return factory.getUserInfoRestTemplate();
//    }

    /*
        JWT 사용
     */
    @Primary
    @Bean
    public RestTemplate getCustomRestTemplate() {
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();
        if (interceptors == null) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }

        return template;
    }


    public static void main(String[] args) {
        SpringApplication.run(License7Application.class, args);
    }
}
