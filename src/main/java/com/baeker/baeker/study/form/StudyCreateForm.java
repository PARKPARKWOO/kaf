package com.baeker.baeker.study.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Data @ToString
@AllArgsConstructor
public class StudyCreateForm {

    @NotBlank
    @Size(min = 2, max = 15)
    private String name;

    @Size(max = 50)
    private String about;

    @NotNull
    @Range(min = 2, max = 20)
    private Integer capacity;
}
