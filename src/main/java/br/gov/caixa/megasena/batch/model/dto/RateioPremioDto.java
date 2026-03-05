package br.gov.caixa.megasena.batch.model.dto;

public record RateioPremioDto(
        String descricaoFaixa,
        Integer faixa,
        Integer numeroDeGanhadores,
        Double valorPremio
) {
}
