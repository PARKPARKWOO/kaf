package com.baeker.baeker.kafka;

import com.baeker.baeker.member.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "${message.topic.name}", groupId = ConsumerConfig.GROUP_ID_CONFIG)
    public void consume(String message) throws IOException {
        log.info("#############메시지 대기중 ###############");
        log.info("message : {}", message);
        ObjectMapper objectMapper = new ObjectMapper();
        MemberDto memberDto = objectMapper.readValue(message, MemberDto.class);
        System.out.println("############# memberDto"+memberDto.getBaekJoonName());
    }
}