package com.baeker.baeker.study.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
public class StudyModifyForm {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 2, max = 15)
    private String name;

    @Size(max = 20)
    private String about;

    @NotNull
    @Range(min = 2, max = 20)
    private Integer capacity;

    public StudyModifyForm(String name, String about, Integer capacity) {
        this.name = name;
        this.about = about;
        this.capacity = capacity;
    }
}
