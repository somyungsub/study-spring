package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.OrganizationRedisRepository;
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
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrganizationRedisRepository orgRedisRepo;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    private Organization checkRedisCache(String organizationId) {
        try {
            return orgRedisRepo.findOrganization(organizationId);
        }
        catch (Exception ex){
            logger.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRedisRepo.saveOrganization(org);
        }catch (Exception ex){
            logger.error("Unable to cache organization {} in Redis. Exception {}", org.getId(), ex);
        }
    }

    public Organization getOrganization(String organizationId){
        logger.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId());

        // 레디스 캐시 조회
        Organization org = checkRedisCache(organizationId);

        // 캐시가 있다면 반환
        if (org!=null){
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, org);
            return org;
        }

        // 캐시가 없다면 조직서비스 호출
        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://zuulservice/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        // 캐시(레디스)에 조직서비스 호출 결과 저장
        org = restExchange.getBody();

        if (org!=null) {
            cacheOrganizationObject(org);
        }

        // 결과 반환
        return org;
    }


}
