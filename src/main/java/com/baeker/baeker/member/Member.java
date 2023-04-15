package com.baeker.baeker.member;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String userId;
    private String name;
    private String password;
    private String provider;
    private String token;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;


    //-- crate method --//
    protected static Member createMember(String userId, String name, String password, String provider, String token) {
        return builder()
                .userId(userId)
                .name(name)
                .password(password)
                .provider(provider)
                .token(token)
                .build();
    }


}
