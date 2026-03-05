package br.gov.caixa.megasena.batch.writer;

import br.gov.caixa.megasena.batch.model.MegasenaModel;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Configuration
public class JogosMegasenaWriter {

    private AtomicInteger count = new AtomicInteger(0);


    @Bean
    public JdbcBatchItemWriter<MegasenaModel> jogosRealizadosModelJdbcBatchItemWriter(DataSource dataSource) {
        JdbcBatchItemWriter<MegasenaModel> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("""
                    INSERT INTO tb_megasena (concurso, data, bola1, bola2, bola3, bola4, bola5, bola6)
                    VALUES (:concurso, :data, :bola1, :bola2, :bola3, :bola4, :bola5, :bola6)
                    ON CONFLICT (concurso) DO UPDATE SET
                        data = EXCLUDED.data,
                        bola1 = EXCLUDED.bola1,
                        bola2 = EXCLUDED.bola2,
                        bola3 = EXCLUDED.bola3,
                        bola4 = EXCLUDED.bola4,
                        bola5 = EXCLUDED.bola5,
                        bola6 = EXCLUDED.bola6
                """);
        writer.setItemSqlParameterSourceProvider(item -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("concurso", item.getConcurso());
            params.addValue("data", item.getData());
            params.addValue("bola1", item.getBola1());
            params.addValue("bola2", item.getBola2());
            params.addValue("bola3", item.getBola3());
            params.addValue("bola4", item.getBola4());
            params.addValue("bola5", item.getBola5());
            params.addValue("bola6", item.getBola6());
            return params;
        });
        writer.afterPropertiesSet();
        return writer;
    }

    @Bean
    public FlatFileItemWriter<String> jogosGeradosWriter() {
        FlatFileItemWriter<String> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("resultados/sorteios_gerados.txt"));
        writer.setLineAggregator(item -> item);
        return writer;
    }

    @Bean
    public FlatFileItemWriter<List<Integer>> combinacaoWriter() {
        FlatFileItemWriter<List<Integer>> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("resultados/todas_combinacoes.txt"));
        writer.setHeaderCallback(item -> item.append("Sorteio Premiado - Gerado em " + LocalDate.now().toString()));
        writer.setLineAggregator(list -> {
            String result = list.stream()
                    .map(i -> String.format("%02d", i))
                    .collect(Collectors.joining(","));
            return MessageFormat.format("Jogo nº {0} => {1}",count.incrementAndGet(), result);
        });
        writer.setFooterCallback(footer -> footer.append("Total de Jogos lançados " + count.get()));
        writer.setShouldDeleteIfExists(true);
        writer.setAppendAllowed(false);
        return writer;
    }
}
