package br.gov.caixa.megasena.batch.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableBatchProcessing
@RequiredArgsConstructor
public class LoteriasJobMasterConfig {

    private final Job quinaJob;
    private final Job megasenaJob;
    private final Job lotoFacilJob;
    private final JobLauncher jobLauncher;

    @Bean
    public Job loteriasJobMaster(JobRepository jobRepository) {
        return new JobBuilder("loteriasJobMaster", jobRepository)
                .start(jobStep(jobRepository, megasenaJob))
                .next(jobStep(jobRepository, quinaJob))
                .next(jobStep(jobRepository, lotoFacilJob))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    private Step jobStep(JobRepository jobRepository, Job job) {
        return new JobStepBuilder(new StepBuilder(job.getName() + "Step", jobRepository))
                .job(job)
                .launcher(jobLauncher)
                .build();
    }
}
