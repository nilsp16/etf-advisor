package de.dhbwravensburg.etfadvisor.repository;

import de.dhbwravensburg.etfadvisor.entity.WatchlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistEntryRepository extends JpaRepository<WatchlistEntry,Long> {

    List<WatchlistEntry> findByEtfId(Long etfId);
    Boolean existsByEtfId(Long etfId);

}
