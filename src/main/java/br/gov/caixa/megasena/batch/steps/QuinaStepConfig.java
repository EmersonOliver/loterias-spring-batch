package br.gov.caixa.megasena.batch.steps;

import br.gov.caixa.megasena.batch.model.QuinaModel;
import br.gov.caixa.megasena.batch.processor.GeradorJogosProcessor;
import br.gov.caixa.megasena.batch.reader.CombinacaoHistoricoReader;
import br.gov.caixa.megasena.batch.reader.CombinacaoProbabilisticaReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static br.gov.caixa.megasena.batch.configuration.QuinaJobConfig.HEADER_FILE_QUINA;

@Configuration
public class QuinaStepConfig {

    @Value("${quantidade.sorteios.megasena}")
    private String quantidadeJogos;

    @Value("${file.path.output}")
    private String filePathOutput;

    @Value("${data.quina.jogo}")
    private String dataJogo;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH.mm.ss");


    @Bean
    public Step quinaStep(JobRepository jobRepository,
                          DataSource dataSource,

                          PlatformTransactionManager transactionManager) {
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("quina-historico-step", jobRepository)
                .<List<Integer>, String>chunk(1, transactionManager)
                .reader(new CombinacaoHistoricoReader(findAll(dataSource), 20))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("quinaJogoWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "quina/historico/jogos-qn-historico-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append(HEADER_FILE_QUINA))
                        .lineAggregator(item -> item)
                        .footerCallback(footer -> footer.append("Boa Sorte"))
                        .build())
                .build();
    }

    @Bean
    public Step gerarJogosQuinaPorProbabilidade(JobRepository jobRepository,
                                                PlatformTransactionManager transactionManager,
                                                DataSource dataSource) {
        final AtomicInteger count = new AtomicInteger(0);
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("quina-probabilistica-step", jobRepository)
                .<List<Integer>, String>chunk(100, transactionManager)
                .reader(new CombinacaoProbabilisticaReader(findAll(dataSource)))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("jogosQuinaWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "quina/jogos-qn-probabilidade-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append(HEADER_FILE_QUINA))
                        .lineAggregator(item -> item)
                        .footerCallback(footer -> footer.append("Boa Sorte"))
                        .build())
                .build();
    }

    private List<QuinaModel> findAll(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        return jdbcTemplate.query("SELECT * FROM tb_quina where EXTRACT(DAY FROM data) = " + Integer.parseInt(dataJogo),
                QuinaModel::mapper);
    }

}
