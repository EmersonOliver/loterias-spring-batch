package br.gov.caixa.megasena.batch.reader;

import br.gov.caixa.megasena.batch.model.LotofacilModel;
import br.gov.caixa.megasena.batch.model.MegasenaModel;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class LotofacilReaderConfig {

    @Bean
    public JdbcPagingItemReader<LotofacilModel> readerJogosLotofacil(DataSource dataSource) {
        JdbcPagingItemReader<LotofacilModel> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(1000);
        reader.setPageSize(1000);
        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("tb_lotofacil");
        queryProvider.setSortKeys(Map.of("concurso", Order.DESCENDING));

        reader.setQueryProvider(queryProvider);
        reader.setRowMapper(LotofacilModel::mapper);
        return reader;
    }
}
