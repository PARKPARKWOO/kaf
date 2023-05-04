package com.baeker.baeker.member.embed;

import lombok.*;

@Data
@NoArgsConstructor // 더미 생성자용 어노테이션
@AllArgsConstructor
public class BaekJoonDto {

    private int bronze;
    private int sliver;
    private int gold;
    private int platinum;
    private int diamond;
    private int ruby;

}
