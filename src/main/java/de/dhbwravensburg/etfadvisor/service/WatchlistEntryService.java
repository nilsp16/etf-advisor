package de.dhbwravensburg.etfadvisor.service;

import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryRequest;
import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import de.dhbwravensburg.etfadvisor.entity.User;
import de.dhbwravensburg.etfadvisor.entity.WatchlistEntry;
import de.dhbwravensburg.etfadvisor.exceptions.DuplicateWatchlistEntryException;
import de.dhbwravensburg.etfadvisor.exceptions.EtfNotFoundException;
import de.dhbwravensburg.etfadvisor.exceptions.WatchlistEntryNotFoundException;
import de.dhbwravensburg.etfadvisor.mapper.WatchlistEntryMapper;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import de.dhbwravensburg.etfadvisor.repository.UserRepository;
import de.dhbwravensburg.etfadvisor.repository.WatchlistEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WatchlistEntryService {

    private final WatchlistEntryRepository repository;
    private final EtfRepository etfRepository;
    private final UserRepository userRepository;

    public WatchlistEntryService (WatchlistEntryRepository repository, EtfRepository etfRepository, UserRepository userRepository){
        this.repository = repository;
        this.etfRepository=etfRepository;
        this.userRepository=userRepository;
    }

    public List<WatchlistEntry> findAll(){
        var user = getCurrentUser();
        return this.repository.findByUserId(user.getId()); }

    public List<WatchlistEntry> findByEtfId(Long etfId){return this.repository.findByEtfId(etfId);}

    @Transactional
    public WatchlistEntry create(Long etfId, WatchlistEntryRequest entryRequest){
        var user = getCurrentUser();
        if (repository.existsByEtfIdAndUserId(etfId, user.getId())){
            throw  new DuplicateWatchlistEntryException();
        }
        return etfRepository.findById(etfId).map(etf -> {
            WatchlistEntry entry = WatchlistEntryMapper.toEntity(entryRequest,etf);
            entry.setUser(user);
            return repository.save(entry);
        })
                .orElseThrow(()->new EtfNotFoundException(etfId));

    }

    @Transactional
    public void delete(Long id){
        var entry = repository.findById(id).orElseThrow(()->new WatchlistEntryNotFoundException(id));
        var user = getCurrentUser();
        if (!entry.getUser().getId().equals(user.getId())){
           throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Not your watchlist entry");
        }
        repository.deleteById(id);
    }

    private User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }


}
