package br.gov.caixa.megasena.batch.model;

public record RateioPremioDto(
        String descricaoFaixa,
        Integer faixa,
        Integer numeroDeGanhadores,
        Double valorPremio
) {
}
