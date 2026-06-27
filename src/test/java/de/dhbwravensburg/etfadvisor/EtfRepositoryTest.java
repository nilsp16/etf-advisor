package de.dhbwravensburg.etfadvisor;

import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class EtfRepositoryTest {

    @Autowired
    EtfRepository etfRepository;

    @Test
    void findByTickerIgnoreCase_shouldFindEtf(){
        Etf etf = new Etf();
        etf.setName("Test ETF");
        etf.setTicker("TESTXYZ");
        etf.setIsin("TEST123");
        etfRepository.save(etf);

        var result= etfRepository.findByTickerIgnoreCase("testxyz");
        assertEquals("TESTXYZ",result.get().getTicker());
    }

    @Test
    void findByIsin_shouldReturnEmpty_whenNotExists(){

        var result = etfRepository.findByIsin("IEWIQWE034");
        assertTrue(result.isEmpty());
    }
}
