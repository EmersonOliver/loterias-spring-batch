package br.gov.caixa.megasena.batch.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StrategyRepositoryFactory {

    private final Map<String, StrategyRepository<?>> strategies;

    public StrategyRepository<?> getStrategy(String jobName) {
        return switch (jobName) {
            case "megasena" -> strategies.get("megasenaStrategy");
            case "lotofacil" -> strategies.get("lotofacilStrategy");
            default -> throw new IllegalArgumentException("Job não encontrado: " + jobName);
        };
    }
}


