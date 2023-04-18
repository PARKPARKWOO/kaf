package com.baeker.baeker.member.embed;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Programmers {

    private Integer lv1;
    private Integer lv2;
    private Integer lv3;
    private Integer lv4;
    private Integer lv5;

    public Integer totalSolved() {
        return lv1 + lv2 + lv3 + lv4 + lv5;
    }
}
