package com.thoughtmechanix.organization.events.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CustomSource {
  @Output("outboundSource")
  MessageChannel output();
}
