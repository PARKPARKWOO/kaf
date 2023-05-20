package com.baeker.baeker.kafka;

import com.baeker.baeker.member.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class KafkaProducer {

    @Value(value = "${message.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, MemberDto> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(MemberDto memberDto) {
        this.kafkaTemplate.send(topicName, memberDto);
        log.info("message 발송" + memberDto.toString());
    }
}

