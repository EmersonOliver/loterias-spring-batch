package br.gov.caixa.megasena.batch.writer;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class JogosRepetidosFileWriter {
    @Value("${file.path.output}")
    private String filePathOutput;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH.mm.ss");


    @Bean
    public FlatFileItemWriter<String> mapFlatFileItemWriter(){
        LocalDateTime agora = LocalDateTime.now();
        return new FlatFileItemWriterBuilder<String>()
                .name("repetidosWriter")
                .resource(new FileSystemResource(String.format(filePathOutput + "megasena/historico/jogos-repetidos-ms-%s.csv", formatter.format(agora))))
                .lineAggregator(item -> item)
                .build();

    }
}
