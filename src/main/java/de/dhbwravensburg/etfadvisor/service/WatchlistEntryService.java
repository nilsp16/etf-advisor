package de.dhbwravensburg.etfadvisor.service;

import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryRequest;
import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import de.dhbwravensburg.etfadvisor.entity.WatchlistEntry;
import de.dhbwravensburg.etfadvisor.exceptions.DuplicateWatchlistEntryException;
import de.dhbwravensburg.etfadvisor.exceptions.EtfNotFoundException;
import de.dhbwravensburg.etfadvisor.exceptions.WatchlistEntryNotFoundException;
import de.dhbwravensburg.etfadvisor.mapper.WatchlistEntryMapper;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import de.dhbwravensburg.etfadvisor.repository.WatchlistEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WatchlistEntryService {

    private final WatchlistEntryRepository repository;
    private final EtfRepository etfRepository;

    public WatchlistEntryService (WatchlistEntryRepository repository, EtfRepository etfRepository){
        this.repository = repository;
        this.etfRepository=etfRepository;
    }

    public List<WatchlistEntry> findAll(){return this.repository.findAllWithEtf(); }

    public List<WatchlistEntry> findByEtfId(Long etfId){return this.repository.findByEtfId(etfId);}

    @Transactional
    public WatchlistEntry create(Long etfId, WatchlistEntryRequest entryRequest){
        if (repository.existsByEtfId(etfId)){
            throw  new DuplicateWatchlistEntryException();
        }
        return etfRepository.findById(etfId).map(etf -> {
            WatchlistEntry entry = WatchlistEntryMapper.toEntity(entryRequest,etf);
            return repository.save(entry);
        })
                .orElseThrow(()->new EtfNotFoundException(etfId));

    }

    @Transactional
    public void delete(Long id){
        if ((!repository.existsByEtfId(id))){
            throw new WatchlistEntryNotFoundException(id);
        }
        repository.deleteById(id);
    }


}
