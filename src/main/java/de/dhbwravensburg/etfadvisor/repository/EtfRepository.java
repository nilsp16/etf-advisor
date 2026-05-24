package de.dhbwravensburg.etfadvisor.repository;

import de.dhbwravensburg.etfadvisor.entity.Etf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtfRepository extends JpaRepository<Etf, Long> {

    List<Etf> findAll();
    List<Etf> findByDividendPolicyIgnoreCase(String dividendPolicy);

    Optional<Etf> findById(Long id);
    Optional<Etf> findByTickerIgnoreCase(String ticker);
    Optional<Etf> findByIsin(String isin);


}
