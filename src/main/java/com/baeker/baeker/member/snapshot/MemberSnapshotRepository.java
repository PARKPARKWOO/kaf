package com.baeker.baeker.member.snapshot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSnapshotRepository extends JpaRepository<MemberSnapshot, Long> {
    List<MemberSnapshot> findByBaekJoonName(String bjName);
}
