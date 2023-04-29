package com.baeker.baeker.member;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.embed.BaekJoon;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.member.form.MemberModifyForm;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
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
    private final StudyService studyService;


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

    //-- find Member 가 리더인 MyStudy 리스트 조회 --//
    public List<MyStudy> getMyStudyOnlyLeader(Member member) {
        List<MyStudy> onlyLeader = new ArrayList<>();
        List<MyStudy> myStudies = member.getMyStudies();

        for (MyStudy myStudy : myStudies)
            if (member.getNickName().equals(myStudy.getStudy().getLeader()))
                onlyLeader.add(myStudy);

        return onlyLeader;
    }


    //-- Security Join ( password 검증 ) --//
    @Transactional
    public RsData<Member> join(MemberJoinForm form) {

        if (!form.getPassword().equals(form.getPassword2()))
            return RsData.of("F-1", "비밀번호가 일치하지 않습니다.");

        return join("Baeker", form.getUsername(), form.getNickName(), form.getAbout(), form.getPassword(), form.getProfileImg(), "", ""); // !!! 일반 로그인 이메일 항목 추가해야 함!!!
    }

    //-- Social Join, Login --//
    @Transactional
    public RsData<Member> whenSocialLogin(String provider, String username, String name, String email, String token, String profileImg) {
        Optional<Member> opMember = getMember(username);

        if (opMember.isPresent())
            return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        return join(provider, username, name, "", "", profileImg, email, token);
    }


    //-- 백준 최신화 --//
    @Transactional
    public RsData<BaekJoon> solve(Long memberId, BaekJoon baekJoon) {
        RsData<Member> memberRs = this.getMember(memberId);
        BaekJoon addedSolve;

        if (memberRs.isFail())
            return RsData.failOf(memberRs.getData().getBaekJoon());

        Member member = memberRs.getData();

        if (member.getBaekJoon() == null)
            addedSolve = member.createSolve(baekJoon);
        else
            addedSolve = member.updateSolve(baekJoon);

        // 더해진 값 반환
        return RsData.successOf(addedSolve);
    }


    //-- Join : Social + Security 실질적인 처리 --//
    private RsData<Member> join(String provider, String username, String name, String about, String password, String profileImg, String email, String token) {

        if (this.getMember(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        if (StringUtils.hasText(password)) {
            password = encoder.encode(password);
        }

        Member member = Member.createMember(provider, username, name, about, password, profileImg, email, token);
        memberRepository.save(member);
        return RsData.of("S-1", "회원가입이 완료되었습니다. \n로그인 해주세요.", member);
    }


    //-- nick name, about, img 수정 --//
    @Transactional
    public RsData<Member> modify(Member member, MemberModifyForm form) {

        Optional<Member> byName = memberRepository.findByNickName(form.getNickName());

        if (!member.getNickName().equals(form.getNickName()))
            if (byName.isPresent())
                return RsData.of("F-1", form.getNickName() + "(은)는 이미 존재하는 이름입니다.");

        List<MyStudy> myStudies = this.getMyStudyOnlyLeader(member);

        Member modifyMember = member.modifyMember(form.getNickName(), form.getAbout(), form.getProfileImg());
        Member saveMember = memberRepository.save(modifyMember);

        return this.modifyLeader(saveMember, myStudies);
    }


    //-- 스터디 리더의 이름이 변경될경우 스터디 리더 변경 --//
    private RsData<Member> modifyLeader(Member member, List<MyStudy> myStudies) {

        if (myStudies.size() == 0 )
            return RsData.of("S-1", "수정이 완료되었습니다.", member);

        for (MyStudy myStudy : myStudies) {
            RsData<Study> studyRs = studyService.modifyLeader(member, myStudy.getStudy().getId());

            if (studyRs.isFail())
                return RsData.of("F-2", studyRs.getMsg());
        }

        return RsData.of("S-1", "수정이 완료되었습니다.", member);
    }

    //-- 스터디 가입 여부 확인 --//
    public boolean isMyStudy(Member member, Study study) {
        List<MyStudy> myStudies = member.getMyStudies();

        for (MyStudy myStudy : myStudies)
            if (!myStudy.getStudy().equals(study))
                return false;

        return true;
    }


    //-- 1~999 랜덤숫자 생성 --//
    public List<Integer> random() {

        List<Integer> random = new ArrayList<>();

        for (int i = 0; i < 10; i++)
            random.add((int) (Math.random() * 999) + 1);

        return random;
    }

}