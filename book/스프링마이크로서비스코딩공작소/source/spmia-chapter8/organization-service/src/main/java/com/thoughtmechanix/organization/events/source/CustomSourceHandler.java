package com.thoughtmechanix.organization.events.source;

import com.thoughtmechanix.organization.events.models.OrganizationChangeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(CustomSource.class)
public class CustomSourceHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomSourceHandler.class);

  @Autowired
  private CustomSource customSource;

  @SendTo("outboundSource")
  public void outTest(OrganizationChangeModel model) {
    logger.debug("Sending Kafka2 message {} ",model);
    customSource.output().send(MessageBuilder.withPayload(model).build());
  }

}
