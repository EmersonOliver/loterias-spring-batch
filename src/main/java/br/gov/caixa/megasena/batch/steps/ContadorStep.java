package br.gov.caixa.megasena.batch.steps;

import br.gov.caixa.megasena.batch.model.MegasenaModel;
import br.gov.caixa.megasena.batch.model.utils.JogosDezenas;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ContadorStep {


    @Bean
    public Step contador(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                         ItemProcessor<JogosDezenas,String> processor,
                         FlatFileItemWriter<String> mapFlatFileItemWriter,
                         JdbcPagingItemReader<MegasenaModel> carregarJogs,
                         DataSource dataSource) {
        return new StepBuilder("contadorStep", jobRepository)
                .<JogosDezenas, String>chunk(1000, transactionManager)
                .reader(carregarJogs)
                .processor(processor)
                .writer(mapFlatFileItemWriter).build();

    }

    @Bean
    public JdbcPagingItemReader<MegasenaModel> carregarJogs(DataSource dataSource) {
        JdbcPagingItemReader<MegasenaModel> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);

        reader.setName("jdbcReader");
        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("tb_megasena");
        queryProvider.setSortKeys(Map.of("concurso", Order.DESCENDING));
        reader.setQueryProvider(queryProvider);
        reader.setPageSize(1000);
        reader.setFetchSize(1000);
        reader.setRowMapper(MegasenaModel::mapper);
        return reader;
    }


}
