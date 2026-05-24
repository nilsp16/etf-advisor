package de.dhbwravensburg.etfadvisor.repository;

import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByEtfId(Long etfId);



}
