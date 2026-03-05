package br.gov.caixa.megasena.batch.steps;

import br.gov.caixa.megasena.batch.model.QuinaModel;
import br.gov.caixa.megasena.batch.processor.GeradorJogosProcessor;
import br.gov.caixa.megasena.batch.reader.CombinacaoProbabilisticaQuinaReader;
import br.gov.caixa.megasena.batch.reader.CombinacaoQuinaItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
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
    public Step quinaLoadJogo(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              FlatFileItemReader<QuinaModel> quinaReaderFile,
                              JdbcBatchItemWriter<QuinaModel> jogosQuinaWriter) {
        return new StepBuilder("quinaLoadStep", jobRepository)
                .<QuinaModel, QuinaModel>chunk(1000, transactionManager)
                .reader(quinaReaderFile)
                .writer(jogosQuinaWriter)
                .build();
    }

    @Bean
    public Step quinaStep(JobRepository jobRepository,
                          DataSource dataSource,

                          PlatformTransactionManager transactionManager) {
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("quinaStep", jobRepository)
                .<List<Integer>, String>chunk(1, transactionManager)
                .reader(new CombinacaoQuinaItemReader(findAll(dataSource), 20))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("quinaJogoWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "quina/historico/jogos-baseado-historico-ms-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append("Jogo,Data,Bola1,Bola2,Bola3,Bola4,Bola5"))
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
        return new StepBuilder("gerarJogosPorProbabilidadeBean", jobRepository)
                .<List<Integer>, String>chunk(100, transactionManager)
                .reader(new CombinacaoProbabilisticaQuinaReader(findAll(dataSource)))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("jogosQuinaWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "quina/jogos-ms-probabilidade-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append("Jogo,Bola1,Bola2,Bola3,Bola4,Bola5"))
                        .lineAggregator(item -> item)
                        .footerCallback(footer -> footer.append("Boa Sorte"))
                        .build())
                .build();
    }

    private List<QuinaModel> findAll(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        return jdbcTemplate.query("SELECT concurso, data, bola1, bola2, bola3, bola4, bola5 FROM tb_quina where EXTRACT(DAY FROM data) = " + Integer.parseInt(dataJogo),
                QuinaModel::mapper);
    }

}
