package com.baeker.baeker.kafka;

import com.baeker.baeker.member.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducer producer;

    @Autowired
    private KafkaTemplate<String , Object> kafkaTemplate;

    @Autowired
    KafkaController(KafkaProducer producer){
        this.producer = producer;
    }


    @PostMapping("/send")
    public String sendMessage(MemberDto message) {
        log.info("message : {}", message);
        this.producer.sendMessage(message);

        return "success";
    }
}
