package de.dhbwravensburg.etfadvisor.config;

import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedEtf(EtfRepository repository){
        return args -> {
            if( repository.count()>0){
                return;
            }
            repository.save(new Etf(null, "Vanguard FTSE All-World", "IE00B3RBWM25",
                    "VWRL", "USD", 0.22, 112.45,
                    "Distributing", 89.10, 115.30, 18.5, "Physical","sell")
            );
            repository.save(new Etf(null, "iShares Core MSCI World", "IE00B4L5Y983",
                            "IWDA", "USD", 0.20, 98.76,
                            "Accumulating", 78.40, 101.20, 62.3, "Physical","buy")
                    );

            repository.save(new Etf(null, "Xtrackers MSCI Emerging Markets", "IE00BTJRMP35",
                            "XMME", "EUR", 0.18, 24.31,
                            "Accumulating", 19.80, 26.10, 4.1, "Physical","hold")
                    );

            repository.save(new Etf(null, "iShares S&P 500", "IE0031442068",
                            "IUSA", "USD", 0.07, 45.67,
                            "Accumulating", 36.20, 47.90, 89.7, "Physical","buy")
                    );

            repository.save(new Etf(null, "Amundi MSCI World", "LU1681043599",
                    "CW8", "EUR", 0.12, 412.30,
                    "Accumulating", 320.50, 425.80, 12.8, "Synthetic","sell")
            );
        };
    }
}
