package com.baeker.baeker.member;

import com.baeker.baeker.base.entity.Score;
import com.baeker.baeker.member.embed.BaekJoon;
import com.baeker.baeker.member.embed.Programmers;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.study.Study;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder(toBuilder = true)
public class Member extends Score {

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String studyId;
    private String nickName;
    private String about;
    private String profileImg;
    private String kakaoProfileImage;
    private String password;
    private String provider;
    private String token;
    private String email;
    private String accessToken;
    private boolean newMember;


    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<MyStudy> myStudies = new ArrayList<>();


    //-- crate method --//
    protected static Member createMember(String provider, String username, String name, String about, String password, String profileImg, String email, String token) {
        return builder()
                .provider(provider)
                .username(username)
                .nickName(name)
                .about(about)
                .password(password)
                .profileImg(profileImg)
                .kakaoProfileImage(profileImg)
                .email(email)
                .accessToken(token)
                .newMember(true)
                .build();
    }

    //-- business logic --//

    // name, about, profileImg 수정 //
    protected Member modifyMember(String name, String about, String img) {
        return this.toBuilder()
                .nickName(name)
                .about(about)
                .profileImg(img)
                .modifyDate(LocalDateTime.now())
                .newMember(false)
                .build();
    }


    // 회원 가입 완료 //
    protected void joinComplete() {
        this.newMember = false;
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
