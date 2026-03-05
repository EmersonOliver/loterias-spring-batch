package br.gov.caixa.megasena.batch.model;

import java.util.List;

public record ConcursoDTO(
        Boolean acumulado,
        String dataApuracao,
        String dataProximoConcurso,

        List<String> dezenasSorteadasOrdemSorteio,
        Boolean exibirDetalhamentoPorCidade,

        Long id,
        Integer indicadorConcursoEspecial,

        List<String> listaDezenas,
        List<String> listaDezenasSegundoSorteio,

        List<Object> listaMunicipioUFGanhadores,
        List<RateioPremioDto> listaRateioPremio,
        List<Object> listaResultadoEquipeEsportiva,

        String localSorteio,
        String nomeMunicipioUFSorteio,
        String nomeTimeCoracaoMesSorte,

        Integer numero,
        Integer numeroConcursoAnterior,
        Integer numeroConcursoFinal_0_5,
        Integer numeroConcursoProximo,
        Integer numeroJogo,

        String observacao,
        Object premiacaoContingencia,

        String tipoJogo,
        Integer tipoPublicacao,
        Boolean ultimoConcurso,

        Double valorArrecadado,
        Double valorAcumuladoConcurso_0_5,
        Double valorAcumuladoConcursoEspecial,
        Double valorAcumuladoProximoConcurso,
        Double valorEstimadoProximoConcurso,
        Double valorSaldoReservaGarantidora,
        Double valorTotalPremioFaixaUm
) {
}
