package br.gov.caixa.megasena.batch.repository;

import br.gov.caixa.megasena.batch.model.entity.Lotofacil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LotofacilRepository extends JpaRepository<Lotofacil, Long> {

    @Query("select MAX(concurso) from Lotofacil")
    Optional<Long> findMaxConcurso();
}
