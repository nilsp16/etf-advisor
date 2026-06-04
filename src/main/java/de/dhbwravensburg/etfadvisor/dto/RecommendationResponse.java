package de.dhbwravensburg.etfadvisor.dto;
import de.dhbwravensburg.etfadvisor.entity.Signal;
import java.time.LocalDateTime;

public record RecommendationResponse (
        Long id,
        Long etfId,
        String etfName,
        Signal signal,
        String reasoning,
        LocalDateTime generatedAt,
        double priceAtGeneration,
        int score
){
}
