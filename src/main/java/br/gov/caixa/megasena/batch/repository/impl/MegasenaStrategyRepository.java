package br.gov.caixa.megasena.batch.repository.impl;

import br.gov.caixa.megasena.batch.model.ConcursoDTO;
import br.gov.caixa.megasena.batch.model.Megasena;
import br.gov.caixa.megasena.batch.repository.MegasenaRepository;
import br.gov.caixa.megasena.batch.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component("megasenaStrategy")
@RequiredArgsConstructor
public class MegasenaStrategyRepository implements StrategyRepository<Megasena> {
    private final MegasenaRepository repository;

    @Override
    public void save(Megasena item) {
        repository.save(item);
    }

    @Override
    public Optional<Long> findMaxConcurso() {
        return repository.findMaxConcurso();
    }

    @Override
    public Megasena mapperItem(ConcursoDTO concurso) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate apuracao = LocalDate.parse(concurso.dataApuracao(), formatter);
        Megasena model = new Megasena();
        model.setConcurso(concurso.numero().longValue());
        model.setData(apuracao);
        model.setBola1(Integer.valueOf(concurso.listaDezenas().get(0)));
        model.setBola2(Integer.valueOf(concurso.listaDezenas().get(1)));
        model.setBola3(Integer.valueOf(concurso.listaDezenas().get(2)));
        model.setBola4(Integer.valueOf(concurso.listaDezenas().get(3)));
        model.setBola5(Integer.valueOf(concurso.listaDezenas().get(4)));
        model.setBola6(Integer.valueOf(concurso.listaDezenas().get(5)));
        return model;
    }
}
