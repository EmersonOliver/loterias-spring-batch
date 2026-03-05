package br.gov.caixa.megasena.batch.steps;

import br.gov.caixa.megasena.batch.tasklet.AtualizarConcursosLoteriasTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LoteriasStepConfiguration {

    @Value("${quantidade.sorteios.megasena}")
    private String quantidadeJogos;

    @Value("${file.path.output}")
    private String filePathOutput;

    @Value("${data.megasena.jogo}")
    private String dataJogo;

    private final AtualizarConcursosLoteriasTasklet atualizarConcursosLoteriasTasklet;

    @Bean
    public Step alimentarBase(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("loterias-base-step", jobRepository)
                .tasklet(atualizarConcursosLoteriasTasklet, transactionManager)
                .build();
    }
}
