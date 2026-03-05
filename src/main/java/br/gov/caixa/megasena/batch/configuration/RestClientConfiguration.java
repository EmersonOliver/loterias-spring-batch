package br.gov.caixa.megasena.batch.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Value("${api.loterias.url}")
    private String uri;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(uri)
                .build();
    }



}
