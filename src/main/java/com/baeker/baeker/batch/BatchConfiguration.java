package com.baeker.baeker.batch;

import com.baeker.baeker.base.event.BaekJoonEvent;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.kafka.KafkaController;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberDto;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.solvedApi.SolvedApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {

    private final SolvedApiService solvedApiService;
    private final KafkaController kafkaController;
    private final MemberService memberService;

    @Bean
    public Job testJob(JobRepository jobRepository, Step stepSolved) {
        return new JobBuilder("solved", jobRepository)
                .start(stepSolved)
                .build();
    }
    @Bean
    public Step stepSolved(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(tasklet, transactionManager).build();
    }

    @Bean
    public Tasklet tasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("멤버별 solved count 테스크렛");
            List<MemberDto> memberList = memberService.getAll().getData();
            for (MemberDto member : memberList) {
                try {
                    System.out.println(member.getBaekJoonName());
                    Optional<Integer> Bronze = solvedApiService.getSolvedCount(member, 1, 6);
                    if (Bronze.get() == -1) {
                        continue;
                    }
                    int bronze = Bronze.get() - member.getBronze();
                    Thread.sleep(1000);

                    int Silver = solvedApiService.getSolvedCount(member, 6, 11).get() - member.getSilver();
                    Thread.sleep(1000);

                    int Gold = solvedApiService.getSolvedCount(member, 11, 16).get() - member.getGold();
                    Thread.sleep(1000);

                    int Platinum = solvedApiService.getSolvedCount(member, 16, 21).get() - member.getPlatinum();
                    Thread.sleep(1000);

                    int Diamond = solvedApiService.getSolvedCount(member, 21, 26).get() - member.getDiamond();
                    Thread.sleep(1000);

                    int Ruby = solvedApiService.getSolvedCount(member, 26, 31).get() - member.getRuby();
                    Thread.sleep(1000);

                    BaekJoonDto dto = new BaekJoonDto(bronze , Silver, Gold, Platinum, Diamond, Ruby);
                    MemberDto memberDto = new MemberDto();
                    memberDto.setBaekJoonName(member.getBaekJoonName());
                    memberDto.setId(member.getId());
                    memberDto.setBronze(bronze);
                    memberDto.setSilver(Silver);
                    memberDto.setGold(Gold);
                    memberDto.setPlatinum(Platinum);
                    memberDto.setDiamond(Diamond);
                    memberDto.setRuby(Ruby);
                    kafkaController.sendMessage(memberDto.getBaekJoonName());
                } catch (NullPointerException e) {
                    log.info("###############" + e + "###############");
                    e.printStackTrace();
                }
            }
            return RepeatStatus.FINISHED;
        } );
    }
}

