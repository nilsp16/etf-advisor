package de.dhbwravensburg.etfadvisor;

import de.dhbwravensburg.etfadvisor.dto.EtfRequest;
import de.dhbwravensburg.etfadvisor.dto.EtfResponse;
import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.mapper.EtfMapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EtfMapperTest {
    @Test
    void toResponse_shouldMapAllFields(){
        Etf etf = new Etf(1L, "Vanguard FTSE All-World", "IE00B3RBWM25",
                "VT", "USD", 0.22, 112.45,
                "Distributing", 89.10, 115.30, 18.5, "Physical");

        var response = EtfMapper.toResponse(etf);

        assertEquals(1L, response.id());
        assertEquals("Vanguard FTSE All-World", response.name());
        assertEquals("IE00B3RBWM25", response.isin());
        assertEquals("VT", response.ticker());
        assertEquals(0.22, response.ter());
    }

    @Test
    void toEntity_shouldMapAllFields(){
        EtfRequest etf = new EtfRequest( "Vanguard FTSE All-World", "IE00B3RBWM25",
                "VT", "USD", 0.22, 112.45,
                "Distributing", 89.10, 115.30, 18.5, "Physical");

        var entity = EtfMapper.toEntity(null, etf);

        assertEquals("Vanguard FTSE All-World", entity.getName());
        assertEquals("IE00B3RBWM25", entity.getIsin());
        assertEquals("VT", entity.getTicker());
        assertEquals(0.22, entity.getTer());
    }

}
