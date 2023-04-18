package com.baeker.baeker.member.embed;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@Builder(toBuilder = true)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class BaekJoon {

    private Integer bronze;
    private Integer sliver;
    private Integer gold;
    private Integer platinum;
    private Integer diamond;
    private Integer ruby;
}
