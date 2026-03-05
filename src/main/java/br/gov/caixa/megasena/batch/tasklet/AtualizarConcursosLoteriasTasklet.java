package br.gov.caixa.megasena.batch.tasklet;

import br.gov.caixa.megasena.batch.model.ConcursoDTO;
import br.gov.caixa.megasena.batch.repository.StrategyRepository;
import br.gov.caixa.megasena.batch.repository.StrategyRepositoryFactory;
import br.gov.caixa.megasena.batch.service.ConcursoClientService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AtualizarConcursosLoteriasTasklet implements Tasklet {

    private final RestClient restClient;
    private final ConcursoClientService concursoClientService;
    private final StrategyRepositoryFactory factory;
    private StrategyRepository repository;

    @Override
    @SneakyThrows
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        var execution = contribution.getStepExecution();
        String jobName = execution.getJobExecution().getJobInstance().getJobName();
        ConcursoDTO ultimo = restClient.get()
                .uri("/"+jobName)
                .retrieve()
                .body(ConcursoDTO.class);


        repository = factory.getStrategy(jobName);
        long ultimoAPI = ultimo.numero();
        long ultimoBanco = (long) repository.findMaxConcurso().orElse(0L);

        log.info("### Ultimo concurso realizado {}, ultimo concurso salvo pela aplicação {}, proximo concurso sera {} em {}", ultimoAPI,
                ultimoBanco, ultimo.numeroConcursoProximo(),
                ultimo.dataProximoConcurso());

        for (long numero = ultimoBanco + 1; numero <= ultimoAPI; numero++) {
            try {
                log.info("Buscando concurso {}", numero);
                ConcursoDTO concurso = concursoClientService.buscarConcurso(Integer.parseInt(String.valueOf(numero)), jobName);
                log.info("Concurso {}, dezenas sorteadas {}, data {}", concurso.numero(), concurso.listaDezenas(), concurso.dataApuracao());
                if (concurso == null) {
                    continue;
                }
                var model = repository.mapperItem(concurso);
                this.repository.save(model);

            } catch (Exception e) {
                log.info("Error {}", e.getMessage(), e);
                break;
            }
        }
        return RepeatStatus.FINISHED;
    }
}
