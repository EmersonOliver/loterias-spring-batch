package br.gov.caixa.megasena.batch.repository.impl;

import br.gov.caixa.megasena.batch.model.dto.ConcursoDTO;
import br.gov.caixa.megasena.batch.model.entity.Lotofacil;
import br.gov.caixa.megasena.batch.repository.LotofacilRepository;
import br.gov.caixa.megasena.batch.strategy.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static br.gov.caixa.megasena.batch.model.utils.LoteriasUtils.getDataApuracao;

@RequiredArgsConstructor
@Component("lotofacilStrategy")
public class LotofacilStrategyRepository implements StrategyRepository<Lotofacil> {

    private final LotofacilRepository lotofacilRepository;

    @Override
    public void save(Lotofacil item) {
        lotofacilRepository.save(item);

    }

    @Override
    @Query("select max(concurso) from Lotofacil")
    public Optional<Long> findMaxConcurso() {
        return lotofacilRepository.findMaxConcurso();
    }

    @Override
    public Lotofacil mapperItem(ConcursoDTO concurso) {
        LocalDate apuracao = getDataApuracao(concurso.dataApuracao());
        Lotofacil model = new Lotofacil();
        model.setConcurso(concurso.numero().longValue());
        model.setData(apuracao);
        model.setBola1(Integer.valueOf(concurso.listaDezenas().get(0)));
        model.setBola2(Integer.valueOf(concurso.listaDezenas().get(1)));
        model.setBola3(Integer.valueOf(concurso.listaDezenas().get(2)));
        model.setBola4(Integer.valueOf(concurso.listaDezenas().get(3)));
        model.setBola5(Integer.valueOf(concurso.listaDezenas().get(4)));
        model.setBola6(Integer.valueOf(concurso.listaDezenas().get(5)));
        model.setBola7(Integer.valueOf(concurso.listaDezenas().get(6)));
        model.setBola8(Integer.valueOf(concurso.listaDezenas().get(7)));
        model.setBola9(Integer.valueOf(concurso.listaDezenas().get(8)));
        model.setBola10(Integer.valueOf(concurso.listaDezenas().get(9)));
        model.setBola11(Integer.valueOf(concurso.listaDezenas().get(10)));
        model.setBola12(Integer.valueOf(concurso.listaDezenas().get(11)));
        model.setBola13(Integer.valueOf(concurso.listaDezenas().get(12)));
        model.setBola14(Integer.valueOf(concurso.listaDezenas().get(13)));
        model.setBola15(Integer.valueOf(concurso.listaDezenas().get(14)));
        return model;
    }
}
