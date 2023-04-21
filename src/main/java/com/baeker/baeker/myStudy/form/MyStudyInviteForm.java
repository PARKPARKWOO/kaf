package com.baeker.baeker.myStudy.form;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyStudyInviteForm {


    private Long studyId;

    @Size(max = 40)
    private String msg;
}
