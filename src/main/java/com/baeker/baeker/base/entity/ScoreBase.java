package com.baeker.baeker.base.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class ScoreBase extends  BaseEntity{

    int bronze;
    int sliver;
    int gold;
    int diamond;
    int ruby;
    int platinum;

    public int solvedBaekJoon() {
        return bronze + sliver + gold + diamond + ruby + platinum;


    }

}
