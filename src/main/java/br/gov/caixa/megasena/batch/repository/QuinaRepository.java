package br.gov.caixa.megasena.batch.repository;

import br.gov.caixa.megasena.batch.model.entity.Quina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuinaRepository extends JpaRepository<Quina, Long> {
    @Query("select MAX(concurso) from Quina")
    Optional<Long> findMaxConcurso();
}
