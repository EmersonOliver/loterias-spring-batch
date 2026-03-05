package br.gov.caixa.megasena.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuinaJobConfig {

    @Bean
    public Job quinaJob(JobRepository jobRepository,
                        Step quinaLoadJogo,
                        Step quinaStep,
                        Step gerarJogosQuinaPorProbabilidade) {
        return new JobBuilder("quina", jobRepository)
                .start(quinaLoadJogo)
                .next(quinaStep)
                .next(gerarJogosQuinaPorProbabilidade)
                .incrementer(new RunIdIncrementer())
                .build();
    }


}
