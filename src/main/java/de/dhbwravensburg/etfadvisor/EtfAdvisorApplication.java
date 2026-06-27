package de.dhbwravensburg.etfadvisor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(
        title = "ETF Advisor API",
        version = "1.0.0",
        description = "ETF monitoring with live market data and trading recommendations"
))
public class EtfAdvisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtfAdvisorApplication.class, args);
    }

}
