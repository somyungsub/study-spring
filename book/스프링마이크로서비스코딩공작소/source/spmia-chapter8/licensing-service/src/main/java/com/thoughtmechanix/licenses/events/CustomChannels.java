package com.thoughtmechanix.licenses.events;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannels {
  @Input("inboundOrgChanges")   // 메서드 레벨 채널이름 정의 (사용자 정의 -> application.yml에 명시해야함)
  SubscribableChannel orgs();
}
