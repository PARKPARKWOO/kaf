package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.rule.RuleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRuleService {

    private final StudyRuleRepository studyRuleRepository;


    public RsData<StudyRule> create(StudyRuleForm studyRuleForm) {
        StudyRule studyRule = StudyRule.builder()
                .name(studyRuleForm.getName())
                .about(studyRuleForm.getAbout())
                .build();
        studyRuleRepository.save(studyRule);
        return RsData.of("S-1", "스터디 규칙 생성완료." , studyRule);
    }

    public RsData<StudyRule> getStudyRule(Long id) {
        Optional<StudyRule> rs = studyRuleRepository.findById(id);
        return rs.map(RsData::successOf).orElseGet(() -> RsData.of("F-1", "없습니다."));
    }

    public void modify(StudyRule studyRule, StudyRuleForm studyRuleForm) {
        StudyRule modifyRule = studyRule.toBuilder()
                .name(studyRuleForm.getName())
                .about(studyRuleForm.getAbout())
                .build();
        studyRuleRepository.save(modifyRule);
        RsData.of("S-1", "수정되었습니다.", modifyRule);
    }

    public void showModify(StudyRule studyRule, StudyRuleForm studyRuleForm) {
        studyRuleForm.setName(studyRule.getName());
        studyRuleForm.setAbout(studyRule.getAbout());
    }

    public void delete(StudyRule studyRule) {
        studyRuleRepository.delete(studyRule);
        RsData.of("S-1", "삭제되었습니다");
    }
}
