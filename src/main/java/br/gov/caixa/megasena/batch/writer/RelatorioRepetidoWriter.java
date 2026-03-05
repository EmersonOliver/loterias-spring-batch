package br.gov.caixa.megasena.batch.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class RelatorioRepetidoWriter implements ItemWriter<Map<Integer, AtomicInteger>> {

    private final StringBuilder stringBuilder = new StringBuilder();
    @Override
    public void write(Chunk<? extends Map<Integer, AtomicInteger>> chunk) throws Exception {
        log.info("RESULTADOS MAIS SAIDOS");
        chunk.forEach(item->{

         for(Integer key : item.keySet()){
             stringBuilder.append("NUMERO SORTEADO => " + key +"\n");
             stringBuilder.append("NUMERO DE REPETICOES => " + item.get(key)+"\n");
         }

        });
        System.out.println(stringBuilder);
    }
}
