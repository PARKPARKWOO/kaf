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
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class BaekJoon {

    @Builder.Default
    private Integer bronze = 0;
    @Builder.Default
    private Integer sliver = 0;
    @Builder.Default
    private Integer gold = 0;
    @Builder.Default
    private Integer platinum = 0;
    @Builder.Default
    private Integer diamond = 0;
    @Builder.Default
    private Integer ruby = 0;


    //-- business logic --//
    public Integer totalSolved() {
        return bronze + sliver + gold + platinum + diamond + ruby;
    }
}
