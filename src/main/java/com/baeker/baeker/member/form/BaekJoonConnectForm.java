package com.baeker.baeker.member.form;

import lombok.Data;

@Data
public class BaekJoonConnectForm {

    private String BaekJoonName;
    private int sendCode;
    private int verifyCode;
}
