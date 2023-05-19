package com.baeker.baeker.member;

import com.baeker.baeker.member.embed.BaekJoonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String BaekJoonName;
    private int bronze;
    private int silver;
    private int gold;
    private int platinum;
    private int diamond;
    private int ruby;

}

