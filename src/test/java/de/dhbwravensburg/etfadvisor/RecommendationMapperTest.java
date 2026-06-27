package de.dhbwravensburg.etfadvisor;

import de.dhbwravensburg.etfadvisor.dto.EtfRequest;
import de.dhbwravensburg.etfadvisor.dto.RecommendationRequest;
import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import de.dhbwravensburg.etfadvisor.entity.Signal;
import de.dhbwravensburg.etfadvisor.mapper.EtfMapper;
import de.dhbwravensburg.etfadvisor.mapper.RecommendationMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendationMapperTest {
    @Test
    void toResponse_shouldMapAllFields(){
        Etf etf = new Etf();
        etf.setName("Test ETF");
        etf.setTicker("TEST");

        Recommendation recommendation = new Recommendation(1L, etf, Signal.BUY, "Price is low",
                LocalDateTime.of(2026, 6, 15, 12, 0), 150.0, 1);

        var response = RecommendationMapper.toResponse(recommendation);


        assertEquals(Signal.BUY, response.signal());
        assertEquals("Price is low", response.reasoning());
        assertEquals(LocalDateTime.of(2026, 6, 15, 12, 0), response.generatedAt());
        assertEquals(150.0, response.priceAtGeneration());
        assertEquals(1, response.score());

    }

    @Test
    void toEntity_shouldMapAllFields(){
        Etf etf = new Etf();
        etf.setName("Test ETF");
        etf.setTicker("TEST");

        RecommendationRequest request = new RecommendationRequest(1L, Signal.BUY, "Price is low", 150.0);
        var entity = RecommendationMapper.toEntity(request,etf);

        assertEquals(Signal.BUY, entity.getSignal());
        assertEquals("Price is low", entity.getReasoning());
        assertEquals(150.0, entity.getPriceAtGeneration());
        assertEquals(etf, entity.getEtf());
    }
}
