package com.baeker.baeker.studyRule;

import com.baeker.baeker.DataNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

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

    public StudyRule getStudyRule(Long id) {
        Optional<StudyRule> studyRule = studyRuleRepository.findById(id);
        if (studyRule.isPresent()) {
            return studyRule.get();
        } else {
            throw new DataNotFoundException("StudyRule not found");
        }
    }

    public void modify(StudyRule studyRule) {
        StudyRule modifyRule = studyRule.toBuilder().build();

        studyRuleRepository.save(modifyRule);
    }
}
