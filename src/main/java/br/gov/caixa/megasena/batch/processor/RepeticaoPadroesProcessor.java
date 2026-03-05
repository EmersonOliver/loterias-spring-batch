package br.gov.caixa.megasena.batch.processor;

import br.gov.caixa.megasena.batch.model.utils.JogosDezenas;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RepeticaoPadroesProcessor implements ItemProcessor<JogosDezenas, String> {
    private final Map<Integer, AtomicInteger> contador = new ConcurrentHashMap<>();
    @Override
    public String process(JogosDezenas jogosDezenas) throws Exception {
          StringBuilder saida = new StringBuilder();
        for (Integer numero : jogosDezenas.getDezenas()) {
            contador.computeIfAbsent(numero, k -> new AtomicInteger(0)).incrementAndGet();
        }
        saida.append(contador);
        return saida.toString();
    }
}
