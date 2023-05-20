package com.baeker.baeker.kafka;

import com.baeker.baeker.batch.NotFoundException;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberDto;
import com.baeker.baeker.member.MemberRepository;
import com.baeker.baeker.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MemberService service;
    @KafkaListener(topics = "${message.topic.name}", groupId = ConsumerConfig.GROUP_ID_CONFIG)
    public void consume(String message) throws IOException {
        log.info("#############메시지 대기중 ###############");
        log.info("message : {}", message);
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            map = objectMapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new NotFoundException(e + "데이터 없음");
        }
        try {
            Integer memberId = (Integer) map.get("id");
            Long longId = memberId.longValue();
            Member member = service.getMember(longId).getData();
            System.out.println("#####################"+member.getBaekJoonName()+"#####################");
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Member 데이터 없음");
        }
    }
}