package de.dhbwravensburg.etfadvisor;

import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EtfControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    EtfRepository repository;

    @Test
    void getAll_shouldReturn200() throws Exception{
        mockMvc.perform(get("/api/etfs"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_shouldReturn200_whenExists() throws Exception{
        Etf etf = new Etf();
        etf.setName("iShares Core S&P 500");
        etf.setIsin("IE0031442068");
        etf.setTicker("IVV");
        etf.setCurrency("USD");
        etf.setTer(0.07);
        etf.setCurrentPrice(45.67);
        etf.setDividendPolicy("Accumulating");
        etf.setLow52Week(36.20);
        etf.setHigh52Week(47.90);
        etf.setMarketCap(89.7);
        etf.setReplicationMethod("Physical");
        repository.save(etf);

        mockMvc.perform(get("/api/etfs/"+etf.getId())).andExpect(status().isOk());

    }

   @Test
   void getById_shouldReturn404_whenNotExists() throws Exception{
        mockMvc.perform(get("/api/etfs/10000")).andExpect(status().isNotFound());
   }
}
