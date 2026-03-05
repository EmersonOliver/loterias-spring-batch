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

    public static final String HEADER_FILE_QUINA = "SEQ\t,Bola1\t,Bola2\t,Bola3\t,Bola4\t,Bola5\t";

    @Bean
    public Job quinaJob(JobRepository jobRepository,
                        Step alimentarBase,
                        Step quinaStep,
                        Step gerarJogosQuinaPorProbabilidade) {
        return new JobBuilder("quina", jobRepository)
                .start(alimentarBase)
                .next(quinaStep)
                .next(gerarJogosQuinaPorProbabilidade)
                .incrementer(new RunIdIncrementer())
                .build();
    }


}
