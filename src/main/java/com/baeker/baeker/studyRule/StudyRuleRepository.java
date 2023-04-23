package com.baeker.baeker.studyRule;

import com.baeker.baeker.rule.Rule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRuleRepository extends JpaRepository<StudyRule, Long> {
    Optional<StudyRule> findById(Long id);
    Page<StudyRule> findAllByStudyId(Pageable pageable, Long id);

    Optional<StudyRule> findByName(String name);

}
