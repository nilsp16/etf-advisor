package de.dhbwravensburg.etfadvisor.service;

import de.dhbwravensburg.etfadvisor.dto.RecommendationRequest;
import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import de.dhbwravensburg.etfadvisor.mapper.RecommendationMapper;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import de.dhbwravensburg.etfadvisor.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final RecommendationRepository repository;
    private final EtfRepository etfRepository;

    public RecommendationService (RecommendationRepository repository, EtfRepository etfRepository){
        this.repository = repository;
        this.etfRepository=etfRepository;
    }

    public List<Recommendation> findAll(){return this.repository.findAll();}

    public List<Recommendation> findByEtfId(Long etfId){return this.repository.findByEtfId(etfId);}

    public Optional<Recommendation> create(Long etfId, RecommendationRequest recommendationRequest){
        return this.etfRepository.findById(etfId).map(etf -> {
            Recommendation recommendation = RecommendationMapper.toEntity(recommendationRequest,etf);
            return this.repository.save(recommendation);
        });
    }

    public boolean delete(Long id){
        if(!repository.existsById(id)){
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
