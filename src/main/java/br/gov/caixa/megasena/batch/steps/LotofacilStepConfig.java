package br.gov.caixa.megasena.batch.steps;

import br.gov.caixa.megasena.batch.model.LotofacilModel;
import br.gov.caixa.megasena.batch.model.MegasenaModel;
import br.gov.caixa.megasena.batch.processor.GeradorJogosProcessor;
import br.gov.caixa.megasena.batch.reader.CombinacaoHistoricoReader;
import br.gov.caixa.megasena.batch.reader.CombinacaoProbabilisticaReader;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class LotofacilStepConfig {

    @Value("${quantidade.sorteios.megasena}")
    private String quantidadeJogos;

    @Value("${file.path.output}")
    private String filePathOutput;

    @Value("${data.megasena.jogo}")
    private String dataJogo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH.mm.ss");

    @Bean
    public Step gerarJogoLotofacilStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource){
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("lotofacil", jobRepository )
                .< List<Integer>, String >chunk(1000, transactionManager)
                .reader(new CombinacaoProbabilisticaReader(carregarTodosJogosDoBanco(dataSource)))
                .processor(new GeradorJogosProcessor(dataJogo) )
                .writer(new FlatFileItemWriterBuilder<String>()
                .name("jogosWriter")
                .resource(new FileSystemResource(String.format(filePathOutput + "lotofacil/jogos-lf-probabilidade-%s.csv", formatter.format(agora))))
                .headerCallback(header -> header.append("Jogo,Bola1,Bola2,Bola3,Bola4,Bola5,Bola6,Bola7,Bola8,Bola9,Bola10,Bola11,Bola12,Bola13,Bola14,Bola15"))
                .lineAggregator(item -> item)
                .footerCallback(footer -> footer.append("Boa Sorte"))
                .build())
                .build();
    }

    @Bean
    public Step gerarJogoHistoricoLotofacilStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource){
        LocalDateTime agora = LocalDateTime.now();
        return new StepBuilder("lotofacil", jobRepository )
                .< List<Integer>, String >chunk(1000, transactionManager)
                .reader(new CombinacaoHistoricoReader(carregarTodosJogosDoBanco(dataSource),100))
                .processor(new GeradorJogosProcessor(dataJogo))
                .writer(new FlatFileItemWriterBuilder<String>()
                        .name("jogosWriter")
                        .resource(new FileSystemResource(String.format(filePathOutput + "lotofacil/historico/jogos-lf-historico-%s.csv", formatter.format(agora))))
                        .headerCallback(header -> header.append("Jogo,Bola1,Bola2,Bola3,Bola4,Bola5,Bola6,Bola7,Bola8,Bola9,Bola10,Bola11,Bola12,Bola13,Bola14,Bola15"))
                        .lineAggregator(item -> item)
                        .footerCallback(footer -> footer.append("Boa Sorte"))
                        .build())
                .build();
    }

    private List<LotofacilModel> carregarTodosJogosDoBanco(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        return jdbcTemplate.query("SELECT * FROM tb_lotofacil where EXTRACT(DAY FROM data) = "
                + Integer.parseInt(dataJogo), LotofacilModel::mapper);
    }

}
