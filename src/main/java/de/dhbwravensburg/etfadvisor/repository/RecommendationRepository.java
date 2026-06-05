package de.dhbwravensburg.etfadvisor.repository;

import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query("SELECT r FROM Recommendation r JOIN FETCH r.etf WHERE r.etf.id = :etfId")
    List<Recommendation> findByEtfId(@Param("etfId") Long etfId);

    @Query("SELECT r FROM Recommendation r JOIN FETCH r.etf")
    List<Recommendation> findAllWithEtf();
}
