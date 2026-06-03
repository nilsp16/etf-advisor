package de.dhbwravensburg.etfadvisor.dto.alpaca;

public record MarketDataResponse(
        Long etfId,
        String ticker,
        double currentPrice,
        double open,
        double high,
        double low,
        double close,
        long volume
) {
}
