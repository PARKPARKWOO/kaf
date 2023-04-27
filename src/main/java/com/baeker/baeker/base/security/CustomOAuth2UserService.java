package com.baeker.baeker.base.security;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    private String username;
    private String nickName;
    private String profileImage;
    private String email;
    private String token;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        switch (provider) {
            case "NAVER" -> naverPathing(oAuth2User);
            case "KAKAO" -> kakaoPathing(oAuth2User, userRequest);
            default -> username = oAuth2User.getName();
        }

        Member member = memberService.whenSocialLogin(provider, username, nickName, email, token).getData();
        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }

    //-- 네이버 Json mapping --//
    private void naverPathing(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        this.username = (String) response.get("id");
        this.nickName = (String) response.get("nickname");
        this.profileImage = (String) response.get("profile_image");
    }

    //-- 카카오 Json mapping --//
    private void kakaoPathing(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");
        String email = (String) kakaoAccount.get("email");

        this.token = userRequest.getAccessToken().getTokenValue();

        this.username = oAuth2User.getName();
        this.nickName = nickname;
        this.email = email;
    }
}

class CustomOAuth2User extends User implements OAuth2User {
    public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
