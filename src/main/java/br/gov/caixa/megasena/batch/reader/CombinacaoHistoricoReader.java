package br.gov.caixa.megasena.batch.reader;

import br.gov.caixa.megasena.batch.model.utils.JogosDezenas;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.*;
import java.util.stream.Collectors;

public class CombinacaoHistoricoReader implements ItemReader<List<Integer>> {

    private final List<? extends JogosDezenas> historico;
    private final int totalJogosAGerar;
    private int contador = 0;
    private int tamanhoJogo = 0;

    public CombinacaoHistoricoReader(List<? extends JogosDezenas> historico, int totalJogosAGerar) {
        this.historico = historico;
        this.totalJogosAGerar = totalJogosAGerar;
        this.tamanhoJogo = this.historico.get(0).getDezenas().size();
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

    private List<Integer> gerarCombinacaoBaseadaHistorico(List<? extends JogosDezenas> historico) {
        Set<Integer> numerosMaisFrequentes = historico.stream()
                .flatMap(j -> j.getDezenas().stream())
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Integer> base = new ArrayList<>(numerosMaisFrequentes);
        Collections.shuffle(base);
        return base.subList(0, tamanhoJogo);
    }
}
