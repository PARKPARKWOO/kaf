package com.baeker.baeker.base.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class ScoreBase extends  BaseEntity{

    long bronze;
    long sliver;
    long gold;
    long diamond;
    long ruby;
    long platinum;

    public long solvedBaekJoon() {
        return bronze + sliver + gold + diamond + ruby;
    }


}
