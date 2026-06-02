package de.dhbwravensburg.etfadvisor.dto;
import de.dhbwravensburg.etfadvisor.entity.Signal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecommendationRequest (
        @NotNull(message = "etfId must not be null")
        @Positive(message = "etf ID must be positive")
        Long etfId,
        @NotNull(message = "signal must not be null")
        Signal signal,
        @NotBlank(message = "reasoning must not be blank")
        String reasoning,
        @Positive(message = "priceAtGeneration must be positive")
        double priceAtGeneration
){
}
