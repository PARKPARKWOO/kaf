package com.baeker.baeker.member.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberModifyForm {

    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    @Size(max = 20)
    private String about;

    private Integer profileImg;
}
