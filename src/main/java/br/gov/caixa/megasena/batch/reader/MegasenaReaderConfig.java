package br.gov.caixa.megasena.batch.reader;

import br.gov.caixa.megasena.batch.model.MegasenaModel;
import br.gov.caixa.megasena.batch.service.CombinatioGeneration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class MegasenaReaderConfig {

    @Bean
    public FlatFileItemReader<MegasenaModel> jogosRealizadosModelFlatFileItemReader() {
        FlatFileItemReader<MegasenaModel> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("resultados/megasena.csv"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<MegasenaModel>() {{
            setLineTokenizer(new DelimitedLineTokenizer(";") {{
                setNames("concurso", "data", "bola1", "bola2", "bola3", "bola4", "bola5", "bola6");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<MegasenaModel>() {{
                setTargetType(MegasenaModel.class);
                setCustomEditors(Map.of(
                        LocalDate.class, new PropertyEditorSupport() {
                            @Override
                            public void setAsText(String text) {
                                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            }
                        }
                ));
            }});
        }});
        return reader;
    }

    @Bean
    public JdbcPagingItemReader<MegasenaModel> readerJogosJdbcReader(DataSource dataSource) {
        JdbcPagingItemReader<MegasenaModel> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(1000);
        reader.setPageSize(1000);
        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("tb_megasena");
        queryProvider.setSortKeys(Map.of("concurso", Order.DESCENDING));

        reader.setQueryProvider(queryProvider);
        reader.setRowMapper(MegasenaModel::mapper);
        return reader;
    }

    @Bean
    public ItemReader<List<Integer>> combinacaoReader() {
        return new IteratorItemReader<>(new CombinatioGeneration());
    }

    @Bean
    public ItemProcessor<List<Integer>, String> combinacaoToStringProcessor() {
        return lista -> lista.stream()
                .map(i -> String.format("%02d", i))
                .collect(Collectors.joining(","));
    }


}
