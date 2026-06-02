package de.dhbwravensburg.etfadvisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record EtfRequest(

        @NotBlank(message = "Name must not be blank")
        String name,
        @NotBlank(message = "isin must not be blank")
        String isin,
        @NotBlank(message = "ticker must not be blank")
        String ticker,
        @NotBlank(message = "currency must not be blank")
        String currency,
        @PositiveOrZero(message = "ter must be Zero or positive")
        double ter,
        @Positive(message = "currentPrice must be positive")
        double currentPrice,
        @NotBlank(message = "dividend Policy must not be blank")
        String dividendPolicy,
        @Positive(message = "low 52-Week must be positive")
        double low52Week,
        @Positive(message = "high 52-Week must be positive")
        double high52Week,
        @Positive(message = "market Cap must be positive")
        double marketCap,
        @NotBlank(message = "replication Method must not be blank")
        String replicationMethod
) {
}
