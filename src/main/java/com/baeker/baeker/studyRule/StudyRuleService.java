package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudyRuleService {

    private StudyRuleRepository studyRuleRepository;


//    public StudyRule create(@Valid StudyRuleForm studyRuleForm) {
//        StudyRule studyRule = StudyRule.builder()
//                .name(studyRuleForm.getName())
//                .about(studyRuleForm.getAbout())
//                .build();
//        studyRuleRepository.save(studyRule);
//        return studyRule;
//    }

    public Optional<StudyRule> getStudyRule(Long id) {
        return studyRuleRepository.findById(id);
    }

    public void modify(StudyRule studyRule) {
        StudyRule modifyRule = studyRule.toBuilder().build();

        studyRuleRepository.save(modifyRule);
    }
}
