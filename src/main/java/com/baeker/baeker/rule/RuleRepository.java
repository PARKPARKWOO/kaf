package com.baeker.baeker.rule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;

import java.util.Optional;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    Optional<Rule> findByName(String name);
    Page<Rule> findAll(Pageable pageable);

    Page<Rule> findByNameContaining(String keyword,Pageable pageable);
}
