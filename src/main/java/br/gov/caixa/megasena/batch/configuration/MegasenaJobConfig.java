package br.gov.caixa.megasena.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MegasenaJobConfig {

    public static final String HEADER_FILE_MEGASENA = "SEQ\t,Bola1\t,Bola2\t,Bola3\t,Bola4\t,Bola5\t,Bola6\t";


    @Bean
    public Job megasenaJob(JobRepository jobRepository,
                           Step alimentarBase,
                           Step gerarHistoricoJogoMegasenaStep,
                           Step gerarJogosMegasenaPorProbabilidade) {
        return new JobBuilder("megasena", jobRepository)
                .start(alimentarBase)
                .next(gerarHistoricoJogoMegasenaStep)
                .next(gerarJogosMegasenaPorProbabilidade)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
