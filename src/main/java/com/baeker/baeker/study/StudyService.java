package com.baeker.baeker.study;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyRepository;
import com.baeker.baeker.study.form.StudyCreateForm;
import com.baeker.baeker.study.form.StudyModifyForm;
import com.baeker.baeker.studyRule.StudyRule;
import com.baeker.baeker.studyRule.StudyRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    //-- create --//
    @Transactional
    public RsData<Study> create(StudyCreateForm form, Member member) {

        // 스터디 이름 중복검사
        RsData<Study> studyRs = this.getStudy(form.getName());
        if (studyRs.isSuccess())
            return RsData.of("F-1", "는 이미 사용 중입니다.");

        Study study = Study.createStudy(form.getName(), form.getAbout(), form.getCapacity(), member);
        Study saveStudy = studyRepository.save(study);

        return RsData.of("S-1", "새로운 스터디가 개설되었습니다!", saveStudy);
    }


    //-- find by id --//
    public RsData<Study> getStudy(Long id) {
        Optional<Study> byId = studyRepository.findById(id);

        if (byId.isPresent())
            return RsData.successOf(byId.get());

        return RsData.of("F-1", "존재 하지않는 id");
    }

    //-- find by name --//
    public RsData<Study> getStudy(String name) {
        Optional<Study> byName = studyRepository.findByName(name);

        if (byName.isPresent())
            return RsData.successOf(byName.get());

        return RsData.of("F-1", "존재 하지않는 name");
    }

    //-- find all + page --//
    public Page<Study> getAll(int page) {
        ArrayList<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("xp"));

        PageRequest pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return studyRepository.findAll(pageable);
    }

    //-- find all member --//
    public RsData<List<Member>> getAllMember(Long id) {
        List<Member> members = new ArrayList<>();
        RsData<Study> studyRs = this.getStudy(id);

        if (studyRs.isFail()) return RsData.of("F-1", studyRs.getMsg());
        List<MyStudy> myStudies = studyRs.getData().getMyStudies();

        for (MyStudy myStudy : myStudies) {
            members.add(myStudy.getMember());
        }
        return RsData.successOf(members);
    }

    //-- find all --//
    public List<Study> getAll() {
        return studyRepository.findAll();
    }


    //-- 이름, 소개, 최대 인원 변경 --//
    @Transactional
    public RsData<Study> modify(StudyModifyForm form, Long id) {
        RsData<Study> studyRs = this.getStudy(id);

        if (studyRs.isFail()) return studyRs;
        Study study = studyRs.getData();

        if (study.getMyStudies().size() > form.getCapacity())
            return RsData.of("F-2", "최대 인원이 현재 스터디 인원보다 적습니다.");

        Study modifyStudy = study.modifyStudy(form.getName(), form.getAbout(), form.getCapacity());
        studyRepository.save(modifyStudy);
        return RsData.of("S-1", "수정이 완료되었습니다.", modifyStudy);
    }

    //-- 리더 변경 --//
    @Transactional
    public RsData<Study> modifyLeader(Member member, Long id) {
        RsData<Study> studyRs = this.getStudy(id);

        if (studyRs.isFail()) return studyRs;
        Study study = studyRs.getData();

        Study modifyLeader = study.modifyLeader(member.getNickName());
        studyRepository.save(modifyLeader);
        return RsData.of("S-1", "리더가 변경되었습니다.", modifyLeader);
    }

    //-- 경험치 상승 --//
    @Transactional
    public RsData<Study> xpUp(Integer xp, Long id) {
        RsData<Study> studyRs = this.getStudy(id);

        if (studyRs.isFail()) return studyRs;
        Study study = studyRs.getData();

        study.xpUp(xp);
        return RsData.of("S-1", "경험치가 상승했습니다.", study);
    }


    //-- 스터디 삭제 --//
    @Transactional
    public RsData<Study> delete(Long id) {
        RsData<Study> studyRs = this.getStudy(id);

        if (studyRs.isFail())
            return studyRs;

        studyRepository.delete(studyRs.getData());
        return RsData.of("S-1", "스터디가 삭제되었습니다.");
    }

    //-- 스터디원 id 로 찾기 --//
    public RsData<Member> getMember(Long memberId,Long studyId) {
        RsData<List<Member>> studyMemberRs = this.getAllMember(studyId);

        if (studyMemberRs.isFail())
            return RsData.of("F-1", studyMemberRs.getMsg());

        List<Member> members = studyMemberRs.getData();

        for (Member member : members) {
            if (member.getId() == memberId)
                return RsData.successOf(member);
        }

        return RsData.of("F-2", "스터디에 존재하지 않는 회원입니다.");
    }
}
