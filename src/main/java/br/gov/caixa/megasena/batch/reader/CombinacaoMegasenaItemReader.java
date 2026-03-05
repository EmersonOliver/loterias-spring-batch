package br.gov.caixa.megasena.batch.reader;

import br.gov.caixa.megasena.batch.model.MegasenaModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CombinacaoMegasenaItemReader implements ItemReader<List<Integer>> {


    private final List<MegasenaModel> historico;
    private final int totalJogosAGerar;
    private int contador = 0;

    public CombinacaoMegasenaItemReader(List<MegasenaModel> historico, int totalJogosAGerar) {
        this.historico = historico;
        this.totalJogosAGerar = historico.size();
    }

    @Override
    public List<Integer> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (contador >= totalJogosAGerar) {
            return null;
        }
        contador++;
        List<Integer> jogo = gerarCombinacaoBaseadaHistorico(historico);
        jogo.sort(Comparator.naturalOrder());
        return jogo;
    }

    private List<Integer> gerarCombinacaoBaseadaHistorico(List<MegasenaModel> historico) {
        Set<Integer> numerosMaisFrequentes = historico.stream()
                .flatMap(j -> Stream.of(
                        j.getBola1(), j.getBola2(), j.getBola3(),
                        j.getBola4(), j.getBola5(), j.getBola6()))
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Integer> base = new ArrayList<>(numerosMaisFrequentes);
        Collections.shuffle(base);
        return base.subList(0, 6);
    }


}
