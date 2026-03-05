package br.gov.caixa.megasena.batch.steps;

import br.gov.caixa.megasena.batch.model.MegasenaModel;
import br.gov.caixa.megasena.batch.processor.GeradorJogosProcessor;
import br.gov.caixa.megasena.batch.reader.CombinacaoMegasenaItemReader;
import br.gov.caixa.megasena.batch.reader.CombinacaoProbabilisticaMegasenaReader;
import br.gov.caixa.megasena.batch.tasklet.AtualizarConcursosLoteriasTasklet;
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
                                               ItemProcessor<List<Integer>, String> geradorJogosProcessor,
                                               DataSource dataSource) {
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("gerarJogosStepBean", jobRepository)
                .<List<Integer>, String>chunk(100, transactionManager)
                .reader(new CombinacaoMegasenaItemReader(carregarTodosJogosDoBanco(dataSource), 20))
                .processor(geradorJogosProcessor)
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("jogosWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "megasena/historico/jogos-baseado-historico-ms-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append("Jogo,Bola1,Bola2,Bola3,Bola4,Bola5,Bola6"))
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
        return new StepBuilder("gerarJogosPorProbabilidadeBean", jobRepository)
                .<List<Integer>, String>chunk(100, transactionManager)
                .reader(new CombinacaoProbabilisticaMegasenaReader(carregarTodosJogosDoBanco(dataSource)))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("jogosWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "megasena/jogos-ms-probabilidade-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append("Jogo,Data,Bola1,Bola2,Bola3,Bola4,Bola5,Bola6"))
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
