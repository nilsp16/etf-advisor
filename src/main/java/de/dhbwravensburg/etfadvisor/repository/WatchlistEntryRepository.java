package de.dhbwravensburg.etfadvisor.repository;

import de.dhbwravensburg.etfadvisor.entity.WatchlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatchlistEntryRepository extends JpaRepository<WatchlistEntry,Long> {

    @Query("SELECT w FROM WatchlistEntry w JOIN FETCH w.etf WHERE w.etf.id = :etfId")
    List<WatchlistEntry> findByEtfId(@Param("etfId") Long etfId);

    @Query("SELECT w FROM WatchlistEntry w JOIN FETCH w.etf")
    List<WatchlistEntry> findAllWithEtf();

    Boolean existsByEtfId(Long etfId);

}
