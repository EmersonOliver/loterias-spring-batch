package br.gov.caixa.megasena.batch.reader;

import br.gov.caixa.megasena.batch.model.QuinaModel;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
public class QuinaReaderConfig {

    @Bean
    public FlatFileItemReader<QuinaModel> quinaReaderFile() {
        FlatFileItemReader<QuinaModel> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("resultados/quina.csv"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<QuinaModel>() {{
            setLineTokenizer(new DelimitedLineTokenizer(";") {{
                setNames("concurso", "data", "bola1", "bola2", "bola3", "bola4", "bola5");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<QuinaModel>() {{
                setTargetType(QuinaModel.class);
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
}
