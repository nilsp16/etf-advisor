package de.dhbwravensburg.etfadvisor.scheduler;

import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import de.dhbwravensburg.etfadvisor.service.RecommendationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RecommendationScheduler {
    private final EtfRepository etfRepository;
    private final RecommendationService recommendationService;

    public RecommendationScheduler(EtfRepository etfRepository, RecommendationService recommendationService){
        this.etfRepository = etfRepository;
        this.recommendationService = recommendationService;
    }

    @Scheduled(fixedRate = 60000)
    public void timedRecommendation(){
        var listEtf = etfRepository.findAll();
        for (Etf etf : listEtf){
        try{

                recommendationService.generate(etf.getId());

        }catch (Exception e){
            System.out.println(e+"Couldn't generate Recommendation for Etf:"+ etf.getName());
        }
    }

}
}
