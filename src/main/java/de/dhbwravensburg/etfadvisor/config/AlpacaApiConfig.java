package de.dhbwravensburg.etfadvisor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class AlpacaApiConfig {

    @Value("${alpaca.api.base-url}")
    private String baseUrl;

    @Value("${alpaca.api.connect-timeout-ms}")
    private int connectTimeoutMs;

    @Value("${alpaca.api.read-timeout-ms}")
    private int readTimeoutMs;

    @Value("${alpaca.api.key}")
    private String apiKey;

    @Value("${alpaca.api.secret}")
    private String apiSecKey;

    @Bean
    public RestClient alpacaRestClient(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));


        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("APCA-API-KEY-ID", apiKey)
                .defaultHeader("APCA-API-SECRET-ID", apiSecKey)
                .build();
    }
}
