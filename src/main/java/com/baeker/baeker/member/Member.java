package com.baeker.baeker.member;

import com.baeker.baeker.myStudy.MyStudy;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String studyId;
    private String name;
    private String about;
    private String profileImg;
    private String password;
    private String provider;
    private String token;
    private Integer solvedCount;

    @CreatedDate
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;


    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<MyStudy> myStudies = new ArrayList<>();


    //-- crate method --//
    protected static Member createMember(String provider, String username, String name, String about, String password) {
        return builder()
                .provider(provider)
                .username(username)
                .name(name)
                .about(about)
                .password(password)
                .solvedCount(0)
                .build();
    }

    //-- business logic --//

    // solved count 최신화 , study solved count 합산 //
    protected void updateSolve(Integer solvedCount) {
        int addCount = solvedCount - this.solvedCount;

        this.solvedCount = solvedCount;

        if (this.myStudies.size() != 0)
            for (MyStudy myStudy : this.myStudies)
                myStudy.getStudy().updateSolve(addCount);
    }


    //-- create authorize --//
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 회원에게 member 권한 부여 //
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        // admin 권한 부여 //
        if ("admin".equals(username))
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));

        return grantedAuthorities;
    }
}
