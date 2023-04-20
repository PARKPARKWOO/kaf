package com.baeker.baeker.studyRule;

import com.baeker.baeker.rule.Rule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRuleRepository extends JpaRepository<StudyRule, Long> {
    Optional<StudyRule> findById(Long id);
    Page<StudyRule> findAll(Pageable pageable);

    Optional<StudyRule> findByName(String name);
}
