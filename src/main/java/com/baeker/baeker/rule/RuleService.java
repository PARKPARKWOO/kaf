package com.baeker.baeker.rule;

import com.baeker.baeker.base.request.RsData;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RuleService {

    private RuleRepository ruleRepository;

    public RsData<Rule> create(String name, String about, String provider, String difficulty) {
        Rule rule = Rule.builder()
                .name(name)
                .about(about)
                .provider(provider)
                .difficulty(difficulty)
                .build();
        this.ruleRepository.save(rule);
        return RsData.of("S-1", "Rule 생성 완료", rule);
    }

    public RsData<Rule> modify(Rule rule, String name, String about, String provider, String difficulty) {
        Rule rule1 = rule.toBuilder()
                .name(name)
                .about(about)
                .provider(provider)
                .difficulty(difficulty)
                .build();
        ruleRepository.save(rule1);
        return RsData.of("S-1", "규칙이 수정 되었습니다.", rule1);
    }

    public Optional<Rule> getRule(Long id) {
        return ruleRepository.findById(id);
    }

    public RsData<Rule> delete(Rule rule) {
        this.ruleRepository.delete(rule);
        return RsData.of("S-1", "규칙이 삭제 되었습니다.");
    }
}
