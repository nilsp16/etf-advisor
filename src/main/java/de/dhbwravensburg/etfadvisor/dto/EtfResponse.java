package de.dhbwravensburg.etfadvisor.dto;

public record EtfResponse(
         long id,
         String name,
         String isin,
         String ticker,
         String currency,
         double ter,
         double currentPrice,
         String dividendPolicy,
         double low52Week,
         double high52Week,
         double marketCap,
         String replicationMethod,
         String signal
) {
}
