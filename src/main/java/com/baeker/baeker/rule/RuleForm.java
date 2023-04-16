package com.baeker.baeker.rule;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@AllArgsConstructor
public class RuleForm {
    @NotBlank(message = "규칙 이름을 정해주세요.")
    private String name;

    private String about;

    private String provider;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
}
