package br.gov.caixa.megasena.batch.service;

import br.gov.caixa.megasena.batch.model.ConcursoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcursoClientService {

    private final RestClient restClient;


    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000)
    )
    public ConcursoDTO buscarConcurso(int numero, String jobName) {
        return restClient.get().uri(String.format("/%s/{numero}", jobName), numero).retrieve()
                .body(ConcursoDTO.class);
    }
    @Recover
    public ConcursoDTO recover(Exception e, int numero, String jobName) {
       log.info("Houve uma falha  ao requisitar API {}, concurso= {}", jobName, numero);
        return null;
    }


}
