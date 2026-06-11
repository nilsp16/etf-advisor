package de.dhbwravensburg.etfadvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EtfAdvisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtfAdvisorApplication.class, args);
    }

}
