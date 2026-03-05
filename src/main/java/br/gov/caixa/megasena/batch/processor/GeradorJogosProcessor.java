package br.gov.caixa.megasena.batch.processor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class GeradorJogosProcessor implements ItemProcessor<List<Integer>, String> {

    final AtomicInteger count = new AtomicInteger(0);

    private final LocalDateTime agora = LocalDateTime.now();
    private final String dataDoJogo;

    public GeradorJogosProcessor(String dataDoJogo) {
        this.dataDoJogo = dataDoJogo;
    }

    @Override
    public String process(List<Integer> jogo) throws Exception {
        String result = jogo.stream()
                .map(i -> String.format("%02d\t", i))
                .collect(Collectors.joining(","));
        return MessageFormat.format("{0}\t,{1}", count.incrementAndGet(), result);
    }
}
