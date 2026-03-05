package br.gov.caixa.megasena.batch.writer;

import br.gov.caixa.megasena.batch.model.QuinaModel;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;

@Configuration
public class JogosQuinaWriterConfig {

    @Bean
    public JdbcBatchItemWriter<QuinaModel> jogosQuinaWriter(DataSource dataSource){
        JdbcBatchItemWriter<QuinaModel> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("""
                    INSERT INTO tb_quina (concurso, data, bola1, bola2, bola3, bola4, bola5)
                    VALUES (:concurso, :data, :bola1, :bola2, :bola3, :bola4, :bola5)
                    ON CONFLICT (concurso) DO UPDATE SET
                        data = EXCLUDED.data,
                        bola1 = EXCLUDED.bola1,
                        bola2 = EXCLUDED.bola2,
                        bola3 = EXCLUDED.bola3,
                        bola4 = EXCLUDED.bola4,
                        bola5 = EXCLUDED.bola5
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
            return params;
        });
        writer.afterPropertiesSet();
        return writer;
    }

}
