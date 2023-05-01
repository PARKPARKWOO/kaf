package com.baeker.baeker.base.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class Score extends  BaseEntity{

    private Integer bronze;
    private Integer sliver;
    private Integer gold;
    private Integer platinum;
    private Integer diamond;
    private Integer ruby;
    private Integer solvedBaekJoon;
}
