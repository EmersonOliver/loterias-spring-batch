package br.gov.caixa.megasena.batch.strategy;

import br.gov.caixa.megasena.batch.model.dto.ConcursoDTO;

import java.util.Optional;

public interface StrategyRepository<T> {
    void save(T item);

    Optional<Long> findMaxConcurso();

    T mapperItem(ConcursoDTO concurso);
}
