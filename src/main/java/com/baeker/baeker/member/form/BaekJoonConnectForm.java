package com.baeker.baeker.member.form;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class BaekJoonConnectForm {

    private String BaekJoonName;
    private int sendCode;
    private int verifyCode;

    @Email
    private String email;
}
