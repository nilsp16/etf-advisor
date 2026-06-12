package de.dhbwravensburg.etfadvisor.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@PropertySource(value = "file:${user.dir}/Key/MyKeys.env", ignoreResourceNotFound = true)
public class NinjasApiConfig {

    @Value("${ninjas.api.base-url}")
    private String baseUrl;

    @Value("${ninjas.api.connect-timeout-ms}")
    private int connectTimeoutMs;

    @Value("${ninjas.api.read-timeout-ms}")
    private int readTimeoutMs;

    @Value("${ninjas.api.key}")
    private String apiKey;

    @Bean
    public RestClient ninjasRestClient(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));



        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }
}
