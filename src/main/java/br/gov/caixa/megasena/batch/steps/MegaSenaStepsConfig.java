package br.gov.caixa.megasena.batch.steps;

import br.gov.caixa.megasena.batch.model.MegasenaModel;
import br.gov.caixa.megasena.batch.processor.GeradorJogosProcessor;
import br.gov.caixa.megasena.batch.reader.CombinacaoHistoricoReader;
import br.gov.caixa.megasena.batch.reader.CombinacaoProbabilisticaReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
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

import static br.gov.caixa.megasena.batch.configuration.MegasenaJobConfig.HEADER_FILE_MEGASENA;

@Configuration
public class MegaSenaStepsConfig {

    @Value("${quantidade.sorteios.megasena}")
    private String quantidadeJogos;

    @Value("${file.path.output}")
    private String filePathOutput;

    @Value("${data.megasena.jogo}")
    private String dataJogo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH.mm.ss");


    @Bean
    public Step gerarHistoricoJogoMegasenaStep(JobRepository jobRepository,
                                               PlatformTransactionManager transactionManager,
                                               DataSource dataSource) {
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("megasena-historico-step", jobRepository)
                .<List<Integer>, String>chunk(100, transactionManager)
                .reader(new CombinacaoHistoricoReader(carregarTodosJogosDoBanco(dataSource), 20))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("jogosWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "megasena/historico/jogos-baseado-historico-ms-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append(HEADER_FILE_MEGASENA))
                        .lineAggregator(item -> item)
                        .footerCallback(footer -> footer.append("Boa Sorte"))
                        .build())
                .build();
    }

    @Bean
    public Step gerarJogosMegasenaPorProbabilidade(JobRepository jobRepository,
                                                   PlatformTransactionManager transactionManager,
                                                   DataSource dataSource) {
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("megasena-probabilistica-step", jobRepository)
                .<List<Integer>, String>chunk(100, transactionManager)
                .reader(new CombinacaoProbabilisticaReader(carregarTodosJogosDoBanco(dataSource)))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("jogosWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "megasena/jogos-ms-probabilidade-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append(HEADER_FILE_MEGASENA))
                        .lineAggregator(item -> item)
                        .footerCallback(footer -> footer.append("Boa Sorte"))
                        .build())
                .build();
    }

    private List<MegasenaModel> carregarTodosJogosDoBanco(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        return jdbcTemplate.query("SELECT concurso, data, bola1, bola2, bola3, bola4, bola5, bola6 FROM tb_megasena where EXTRACT(DAY FROM data) = " + Integer.parseInt(dataJogo),
                (rs, rowNum) -> new MegasenaModel(
                        rs.getLong("concurso"),
                        rs.getDate("data").toLocalDate(),
                        rs.getInt("bola1"),
                        rs.getInt("bola2"),
                        rs.getInt("bola3"),
                        rs.getInt("bola4"),
                        rs.getInt("bola5"),
                        rs.getInt("bola6")
                ));
    }


}
