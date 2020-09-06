package com.thoughtmechanix.licenses.events.handlers;

import com.thoughtmechanix.licenses.events.CustomChannels;
import com.thoughtmechanix.licenses.events.models.OrganizationChangeModel;
import com.thoughtmechanix.licenses.repository.OrganizationRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


@EnableBinding(CustomChannels.class)  // Application.java에 명시한 부분 삭제 -> Sink.class 대신 사용자정의 인터페이스 사용
public class OrganizationChangeHandler {

  @Autowired
  private OrganizationRedisRepository organizationRedisRepository;

  private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeHandler.class);

  @StreamListener("inboundOrgChanges")  // application.yml에 명시한 내용 일치 (input 대신사용한)
  public void loggerSink(OrganizationChangeModel orgChange) {
    logger.debug("Received a message of type " + orgChange.getType());
    switch (orgChange.getAction()) {
      case "GET":
        logger.debug("Received a GET event from the organization service for organization id {}", orgChange.getOrganizationId());
        break;
      case "SAVE":
        logger.debug("Received a SAVE event from the organization service for organization id {}", orgChange.getOrganizationId());
        break;
      case "UPDATE":
        logger.debug("Received a UPDATE event from the organization service for organization id {}", orgChange.getOrganizationId());
        organizationRedisRepository.deleteOrganization(orgChange.getOrganizationId());
        break;
      case "DELETE":
        logger.debug("Received a DELETE event from the organization service for organization id {}", orgChange.getOrganizationId());
        organizationRedisRepository.deleteOrganization(orgChange.getOrganizationId());
        break;
      default:
        logger.error("Received an UNKNOWN event from the organization service of type {}", orgChange.getType());
        break;
    }
  }
}
