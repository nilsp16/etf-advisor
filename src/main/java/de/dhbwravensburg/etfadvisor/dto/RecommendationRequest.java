package de.dhbwravensburg.etfadvisor.dto;
import de.dhbwravensburg.etfadvisor.entity.Signal;

public record RecommendationRequest (
        Long etfId,
        Signal signal,
        String reasoning,
        double priceAtGeneration
){
}
