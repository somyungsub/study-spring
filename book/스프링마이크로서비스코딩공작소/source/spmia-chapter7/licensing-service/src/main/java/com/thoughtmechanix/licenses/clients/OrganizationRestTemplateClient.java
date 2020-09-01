package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {
    /*
        OAuth 사용
        - OAuth2RestTemplate -> JWT 기반 토큰을 전파하지 않음
     */
//    @Autowired
//    OAuth2RestTemplate restTemplate;

    /*
        JWT
        - 라이센싱 -> 조직 호출
        - JWT 기반 토큰 전파를 위해 RestTemplate 사용 해야함

     */
    @Autowired
    RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    public Organization getOrganization(String organizationId){
        logger.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId());

        // TODO 공부해서 바꿔놓기
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://localhost:5555/api/organization/v1/organizations/{organizationId}",
//                        "http://zuulserver:5555/api/organization/v1/organizations/{organizationId}",
//                        "http://zuulservice:5555/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
