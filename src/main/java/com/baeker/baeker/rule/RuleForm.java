package com.baeker.baeker.rule;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@AllArgsConstructor
public class RuleForm {
    @NotBlank(message = "규칙 이름을 정해주세요.")
    @Size(max = 10, min = 1)
    private String name;

    @Size(max = 30)
    private String about;

    @NotEmpty
    @Size(min = 1, max = 10)
    private String xp;

    @Size(min = 2, max = 2)
    @NotBlank
    private String provider;

    @NotEmpty
    private String difficulty;
}
