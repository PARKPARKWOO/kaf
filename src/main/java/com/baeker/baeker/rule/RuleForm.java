package com.baeker.baeker.rule;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
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

    @Size(min = 2, max = 2)
    @NotBlank(message = "어디를 이용하실 건가요?")
    private String provider;

    @NotBlank(message = "난이도를 설정해주세요.")
    private String difficulty;
}
