package br.gov.caixa.megasena.batch.reader;

import br.gov.caixa.megasena.batch.model.QuinaModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CombinacaoProbabilisticaQuinaReader implements ItemReader<List<Integer>> {

    private final List<QuinaModel> historico;
    private final Map<Integer, Double> probabilidadeNumeros;
    private final int totalJogosAGerar;
    private int contador = 0;
    private final Random random = new Random();

    public CombinacaoProbabilisticaQuinaReader(List<QuinaModel> historico) {
        this.historico = historico;
        this.totalJogosAGerar = historico.size();
        this.probabilidadeNumeros = calcularProbabilidades(historico);
    }

    @Override
    public List<Integer> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (contador >= totalJogosAGerar) {
            return null;
        }
        contador++;
        List<Integer> jogo = gerarCombinacaoProbabilistica();
        jogo.sort(Comparator.naturalOrder());
        return jogo;
    }
    private List<Integer> gerarCombinacaoProbabilistica() {
        List<Integer> jogo = new ArrayList<>();
        Set<Integer> usados = new HashSet<>();
        while (jogo.size() < 5) {
            int numero = sortearNumeroComPeso(probabilidadeNumeros, usados);
            jogo.add(numero);
            usados.add(numero);
        }
        return jogo;
    }
    private int sortearNumeroComPeso(Map<Integer, Double> pesos, Set<Integer> excluidos) {
        double totalPeso = pesos.entrySet().stream()
                .filter(e -> !excluidos.contains(e.getKey()))
                .mapToDouble(Map.Entry::getValue).sum();

        double sorteio = random.nextDouble() * totalPeso;
        double acumulado = 0.0;

        for (Map.Entry<Integer, Double> entry : pesos.entrySet()) {
            if (excluidos.contains(entry.getKey())) continue;

            acumulado += entry.getValue();
            if (acumulado >= sorteio) {
                return entry.getKey();
            }
        }
        return pesos.keySet().stream().filter(k -> !excluidos.contains(k)).findFirst().orElseThrow();
    }

    private Map<Integer, Double> calcularProbabilidades(List<QuinaModel> historico) {
        Map<Integer, Long> frequencias = historico.stream()
                .flatMap(j -> Stream.of(
                        j.getBola1(), j.getBola2(), j.getBola3(),
                        j.getBola4(), j.getBola5()))
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        long totalOcorrencias = frequencias.values().stream().mapToLong(Long::longValue).sum();

        return frequencias.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (double) e.getValue() / totalOcorrencias
                ));
    }
}
