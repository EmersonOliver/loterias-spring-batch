package br.gov.caixa.megasena.batch.repository;

import br.gov.caixa.megasena.batch.model.ConcursoDTO;

import java.util.Optional;

public interface StrategyRepository<T> {
    void save(T item);

    Optional<Long> findMaxConcurso();

    T mapperItem(ConcursoDTO concurso);
}
