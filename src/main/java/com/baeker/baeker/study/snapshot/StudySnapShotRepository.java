package com.baeker.baeker.study.snapshot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySnapShotRepository extends JpaRepository<StudySnapShot, Long> {
    List<StudySnapShot> findByStudyName(String name);
}
