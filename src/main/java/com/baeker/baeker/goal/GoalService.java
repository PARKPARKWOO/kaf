package com.baeker.baeker.goal;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.myStudy.MyStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final MyStudyService myStudyService;

    /**
     * 데이터 삽입
     */
    public RsData<Goal> create() {
        return RsData.of("S-1", "Goal 생성");
    }


    /**
     * 수정
     */
    public void modify() {

    }

    /**
     * 삭제
     */
    public void delete(Goal goal) {
        //my스터디에서도 삭제??
        goal.getStudyRule().getGoals().remove(goal);
        goalRepository.delete(goal);
    }

    /**
     * 조회
     */
    public RsData<Goal> getGoal(Long id) {
        Optional<Goal> optionalGoal = goalRepository.findById(id);
        return optionalGoal.map(goal -> RsData.of("S-1", "Goal 조회", goal))
                .orElseGet(() -> RsData.of("F-1", "Goal 조회 실패"));
    }

    public List<Goal> getGoalAll() {
        return goalRepository.findAll();
    }
}
