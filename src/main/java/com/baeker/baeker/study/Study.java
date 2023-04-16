package com.baeker.baeker.study;

import com.baeker.baeker.myStudy.MyStudy;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Study {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private String about;
    private String leader;
    private Integer xp;
    private Integer capacity;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;


    @Builder.Default
    @OneToMany(mappedBy = "study")
    private List<MyStudy> myStudies = new ArrayList<>();
}
