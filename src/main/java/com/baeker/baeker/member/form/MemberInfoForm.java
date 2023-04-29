package com.baeker.baeker.member.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberInfoForm {

    private String nickName;
    private String about;
    private String profileImg;
}
