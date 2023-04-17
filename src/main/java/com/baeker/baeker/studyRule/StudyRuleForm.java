package com.baeker.baeker.studyRule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyRuleForm {

    @NotBlank
    @Size(min = 2, max = 10)
    private String name;
    @Size(max = 30)
    private String about;

}
