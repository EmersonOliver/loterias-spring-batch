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


    public static final String HEADER_FILE_LOTOFACIL = "SEQ\t,Bola1\t,Bola2\t,Bola3\t,Bola4\t,Bola5\t,Bola6\t,Bola7\t,Bola8\t,Bola9\t,Bola10\t,Bola11\t,Bola12\t,Bola13\t,Bola14\t,Bola15\t";

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
