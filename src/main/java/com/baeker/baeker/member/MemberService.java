package com.baeker.baeker.member;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.form.MemberJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;


    //-- find by name --//
    public Optional<Member> getMember(String username) {
        return memberRepository.findByUsername(username);
    }

    //-- Security Join : password 검증 --//
    @Transactional
    public RsData<Member> join(MemberJoinForm form) {

        if (!form.getPassword().equals(form.getPassword2()))
            return RsData.of("F-1", "비밀번호가 일치하지 않습니다.");

        return join("Baeker", form.getUsername(), form.getName(), form.getPassword());
    }

    //-- Join : Social + Security 실질적인 처리 --//
    private RsData<Member> join(String provider, String username, String name, String password) {

        if (this.getMember(username).isPresent())
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));

        if (StringUtils.hasText(password))
            password = encoder.encode(password);

        Member member = Member.createMember(provider, username, name, password);
        memberRepository.save(member);
        return RsData.of("S-1", "회원가입이 완료되었습니다. \n로그인 해주세요.", member);
    }

    //-- Social login --//
    @Transactional
    public RsData<Member> whenSocialLogin(String provider, String username, String name) {
        Optional<Member> opMember = getMember(username);

        if (opMember.isPresent())
            return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        return join(provider, username, name, "");
    }
}
