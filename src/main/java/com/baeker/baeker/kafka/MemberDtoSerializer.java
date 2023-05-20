package com.baeker.baeker.kafka;

import com.baeker.baeker.exception.NotFoundException;
import com.baeker.baeker.member.MemberDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.SerializeException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MemberDtoSerializer implements Serializer<MemberDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, MemberDto data) {
        if (data == null) {
            throw new NotFoundException("데이터 없음 serialize");
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializeException("Error", e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, MemberDto data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
