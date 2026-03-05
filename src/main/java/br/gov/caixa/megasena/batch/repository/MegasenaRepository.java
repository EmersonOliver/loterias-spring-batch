package br.gov.caixa.megasena.batch.repository;

import br.gov.caixa.megasena.batch.model.Megasena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MegasenaRepository extends JpaRepository<Megasena, Long> {

    @Query("select MAX(concurso) from Megasena")
    Optional<Long> findMaxConcurso();
}
