package com.baeker.baeker.study;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberRepository;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.member.snapshot.MemberSnapshot;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyRepository;
import com.baeker.baeker.study.form.StudyCreateForm;
import com.baeker.baeker.study.form.StudyModifyForm;
import com.baeker.baeker.study.snapshot.StudySnapShot;
import com.baeker.baeker.study.snapshot.StudySnapShotRepository;
import com.baeker.baeker.studyRule.StudyRule;
import com.baeker.baeker.studyRule.StudyRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudySnapShotRepository studySnapShotRepository;
    private final MemberRepository memberRepository;

    /**
     ** 생성 관련 method **
     * create
     */
    //-- create --//
    @Transactional
    public RsData<Study> create(StudyCreateForm form, Member member) {

        if (member.getBaekJoonName() == null)
            return RsData.of("F-1", "백준과 연동이 필요합니다.");

        // 스터디 이름 중복검사
        RsData<Study> studyRs = this.getStudy(form.getName());
        if (studyRs.isSuccess())
            return RsData.of("F-2", "는 이미 사용 중입니다.");

        Study study = Study.createStudy(form.getName(), form.getAbout(), form.getCapacity(), member);
        Study saveStudy = studyRepository.save(study);
        this.createStudySnapshot(study);

        return RsData.of("S-1", "새로운 스터디가 개설되었습니다!", saveStudy);
    }


    /**
     * * 조회 관련 method **
     * find by id
     * find by study name
     * find all member in study
     * find all
     * find all + paging
     */

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

    //-- find all + page --//
    public Page<Study> getAll(int page) {
        ArrayList<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("xp"));

        PageRequest pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return studyRepository.findAll(pageable);
    }

    //-- find all --//
    public List<Study> getAll() {
        return studyRepository.findAll();
    }


    /**
     ** 수정과 삭제 관련 method **
     * 이름, 소개, 최대 인원 수정
     * leader 변경
     * study 가입시 member 의 백준 solved 추가
     * Xp 상승
     * Study 삭제
     */

    //-- 이름, 소개, 최대 인원 수정 --//
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

    //-- 스터디 가입시 맴버의 백준 문제 추가 --//
    @Transactional
    public Study addBaekJoon(Study study, Member member) {

        BaekJoonDto dto = new BaekJoonDto(member.getBronze(), member.getSliver(), member.getGold(), member.getPlatinum(), member.getDiamond(), member.getRuby());
        String today = LocalDate.now().getDayOfWeek().toString().substring(0, 3);

        this.saveSnapshot(study,dto,today);

        Study updateStudy = study.updateBaekJoon(dto);
        return studyRepository.save(updateStudy);
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


    /**
     ** Snapshot 과 Event 처리 관련 Method **
     * Study 최초 생성시 더미 Snapshot 7 개 생성
     * Snapshot 저장
     * Snapshot 삭제
     * 백준 Solved Evnet 처리
     */

    //-- 스터디 생성 시 더미 스냅샷 7개 생성 --//
    private void createStudySnapshot(Study study) {
        for (int i = 6; i >= 0; i--) {
            BaekJoonDto dummy = new BaekJoonDto();
            String dayOfWeek = LocalDateTime.now().minusDays(i).getDayOfWeek().toString();

            StudySnapShot snapShot = StudySnapShot.create(study, dummy, dayOfWeek.substring(0, 3));
            studySnapShotRepository.save(snapShot);
        }
    }

    //-- Snapshot 저장 --//
    private void saveSnapshot(Study study, BaekJoonDto dto, String today) {

        StudySnapShot snapshot = study.getSnapShotList().get(0);

        if (snapshot.getDayOfWeek().equals(today))
            snapshot = snapshot.update(dto);

        else {
            snapshot = StudySnapShot.create(study, dto, today);
            this.deleteSnapshot(study);
        }
        studySnapShotRepository.save(snapshot);
    }

    //-- 스냅샷 삭제 --//
    private void deleteSnapshot(Study study) {
        List<StudySnapShot> snapShotList = study.getSnapShotList();
        StudySnapShot snapshot = snapShotList.get(snapShotList.size() - 1);

        snapShotList.remove(snapshot);
        studySnapShotRepository.delete(snapshot);
    }

    //-- 백준 스케쥴 이벤트 처리 --//
    public void whenBaekJoonEventType(Long id, BaekJoonDto dto) {
        Member member = memberRepository.findById(id).get();
        List<MyStudy> myStudies = member.getMyStudies();
        String today = LocalDate.now().getDayOfWeek().toString().substring(0, 3);

        for (MyStudy myStudy : myStudies) {
            this.saveSnapshot(myStudy.getStudy(), dto, today);

            Study study = myStudy.getStudy().updateBaekJoon(dto);
            studyRepository.save(study);
        }
    }

}