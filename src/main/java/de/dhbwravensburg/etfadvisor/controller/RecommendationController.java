package de.dhbwravensburg.etfadvisor.controller;

import de.dhbwravensburg.etfadvisor.dto.RecommendationRequest;
import de.dhbwravensburg.etfadvisor.dto.RecommendationResponse;
import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import de.dhbwravensburg.etfadvisor.mapper.RecommendationMapper;
import de.dhbwravensburg.etfadvisor.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService){this.recommendationService=recommendationService;}

    @GetMapping
    public List<RecommendationResponse>getAll(){
        return this.recommendationService.findAll().stream()
                .map(RecommendationMapper::toResponse)
                .toList();
    }

    @GetMapping("/etf/{etfId}")
    public List<RecommendationResponse> getByEtfId(@PathVariable Long etfId){
        return this.recommendationService.findByEtfId(etfId).stream()
                .map(RecommendationMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<RecommendationResponse> save(@Valid @RequestBody RecommendationRequest recommendationRequest){
        var recommendation = recommendationService.create(recommendationRequest.etfId(),recommendationRequest);
        var response = RecommendationMapper.toResponse(recommendation);
        return ResponseEntity.created(URI.create("/api/recommendations/"+response.id())).body(response);

    }
    @PostMapping("/generate/{etfId}")
    public  ResponseEntity<RecommendationResponse>generate(@PathVariable Long etfId){
        var recommendation = recommendationService.generate(etfId);
        var response = RecommendationMapper.toResponse(recommendation);
        return ResponseEntity.created(URI.create("/api/recommendations/"+response.id())).body(response);
    }
}
