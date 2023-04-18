package com.baeker.baeker.member;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.form.MemberJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;


    //-- find by username --//
    public Optional<Member> getMember(String username) {
        return memberRepository.findByUsername(username);
    }

    //-- find by id --//
    public RsData<Member> getMember(Long id) {
        Optional<Member> byId = memberRepository.findById(id);

        if (byId.isPresent())
            return RsData.successOf(byId.get());

        return RsData.of("F-1", "존재하지 않는 id 입니다.");
    }

    //-- find all --//
    public RsData<List<Member>> getAll() {
        List<Member> memberList = memberRepository.findAll();

        if (memberList.size() == 0)
            return RsData.failOf(memberList);

        return RsData.of("S-1", "Size = {}" + memberList.size(), memberList);
    }

    //-- find all + paging --//
    public Page<Member> getAll(int page) {
        ArrayList<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return memberRepository.findAll(pageable);
    }


    //-- Security Join ( password 검증 ) --//
    @Transactional
    public RsData<Member> join(MemberJoinForm form) {

        if (!form.getPassword().equals(form.getPassword2()))
            return RsData.of("F-1", "비밀번호가 일치하지 않습니다.");

        return join("Baeker", form.getUsername(), form.getName(), form.getAbout(), form.getPassword());
    }

    //-- Social Join, Login --//
    @Transactional
    public RsData<Member> whenSocialLogin(String provider, String username, String name) {
        Optional<Member> opMember = getMember(username);

        if (opMember.isPresent())
            return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        return join(provider, username, name, "", "");
    }


    //-- 해결한 문제 최신화 --//
    @Transactional
    public RsData<Member> updateSolve(Long memberId, Integer solvedCount) {
        RsData<Member> memberRs = this.getMember(memberId);

        if (memberRs.isFail())
            return RsData.failOf(memberRs.getData());

        Member member = memberRs.getData();
        member.updateSolve(solvedCount);

        return RsData.successOf(member);
    }


    //-- Join : Social + Security 실질적인 처리 --//
    private RsData<Member> join(String provider, String username, String name, String about, String password) {
        Member member = Member.createMember(provider, username, name, about, password);

        if (this.getMember(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        if (StringUtils.hasText(password)) {
            password = encoder.encode(password);
        }

        memberRepository.save(member);
        return RsData.of("S-1", "회원가입이 완료되었습니다. \n로그인 해주세요.", member);
    }

}