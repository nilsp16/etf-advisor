package de.dhbwravensburg.etfadvisor.mapper;

import de.dhbwravensburg.etfadvisor.dto.RecommendationRequest;
import de.dhbwravensburg.etfadvisor.dto.RecommendationResponse;
import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.entity.Recommendation;

import java.time.LocalDateTime;

public final class RecommandationMapper {

    private RecommandationMapper(){}

    public static Recommendation toEntity( RecommendationRequest request, Etf etf){
        return new Recommendation(
                null,
                etf,
                request.signal(),
                request.reasoning(),
                LocalDateTime.now(),
                request.priceAtGeneration()
        );
    }

    public static RecommendationResponse toResponse(Recommendation recommendation){
        return new RecommendationResponse(
                recommendation.getId(),
                recommendation.getEtf().getId(),
                recommendation.getEtf().getName(),
                recommendation.getSignal(),
                recommendation.getReasoning(),
                recommendation.getGeneratedAt(),
                recommendation.getPriceAtGeneration()
        );
    }
}
