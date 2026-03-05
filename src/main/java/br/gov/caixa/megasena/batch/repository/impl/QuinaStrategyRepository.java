package br.gov.caixa.megasena.batch.repository.impl;

import br.gov.caixa.megasena.batch.model.dto.ConcursoDTO;
import br.gov.caixa.megasena.batch.model.entity.Quina;
import br.gov.caixa.megasena.batch.repository.QuinaRepository;
import br.gov.caixa.megasena.batch.strategy.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static br.gov.caixa.megasena.batch.model.utils.LoteriasUtils.getDataApuracao;

@Component("quinaStrategy")
@RequiredArgsConstructor
public class QuinaStrategyRepository implements StrategyRepository<Quina> {

    private final QuinaRepository repository;

    @Override
    public void save(Quina item) {
        this.repository.save(item);
    }

    @Override
    public Optional<Long> findMaxConcurso() {
        return repository.findMaxConcurso();
    }

    @Override
    public Quina mapperItem(ConcursoDTO concurso) {
        LocalDate apuracao = getDataApuracao(concurso.dataApuracao());
        var model = new Quina();
        model.setConcurso(concurso.numero().longValue());
        model.setData(apuracao);
        model.setBola1(Integer.valueOf(concurso.listaDezenas().get(0)));
        model.setBola2(Integer.valueOf(concurso.listaDezenas().get(1)));
        model.setBola3(Integer.valueOf(concurso.listaDezenas().get(2)));
        model.setBola4(Integer.valueOf(concurso.listaDezenas().get(3)));
        model.setBola5(Integer.valueOf(concurso.listaDezenas().get(4)));
        return model;
    }
}
