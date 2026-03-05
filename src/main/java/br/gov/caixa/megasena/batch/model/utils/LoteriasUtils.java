package br.gov.caixa.megasena.batch.model.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LoteriasUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate getDataApuracao(String data) {
        if (data == null) {
            log.error("Data de Apuracao veio nula");
            throw new RuntimeException("Data de apuracao veio vazia!");
        }
        LocalDate apuracao = LocalDate.parse(data, formatter);
        log.info("Data de Apuracao {}", apuracao);
        return apuracao;
    }
}
