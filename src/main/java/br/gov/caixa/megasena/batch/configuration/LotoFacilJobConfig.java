package br.gov.caixa.megasena.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LotoFacilJobConfig {


    @Bean
    public Job lotoFacilJob(JobRepository jobRepository,
                            Step alimentarBase,
                            Step gerarJogoLotofacilStep,
                            Step gerarJogoHistoricoLotofacilStep) {
        return new JobBuilder("lotofacil", jobRepository)
                .start(alimentarBase)
                .next(gerarJogoLotofacilStep)
                .next(gerarJogoHistoricoLotofacilStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

}
