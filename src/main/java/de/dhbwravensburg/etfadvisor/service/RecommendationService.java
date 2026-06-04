package de.dhbwravensburg.etfadvisor.service;

import de.dhbwravensburg.etfadvisor.client.AlpacaMarketDataClient;
import de.dhbwravensburg.etfadvisor.dto.RecommendationRequest;
import de.dhbwravensburg.etfadvisor.entity.Recommendation;
import de.dhbwravensburg.etfadvisor.entity.Signal;
import de.dhbwravensburg.etfadvisor.exceptions.EtfNotFoundException;
import de.dhbwravensburg.etfadvisor.mapper.RecommendationMapper;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import de.dhbwravensburg.etfadvisor.repository.RecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final RecommendationRepository repository;
    private final EtfRepository etfRepository;
    private final AlpacaMarketDataClient client;

    public RecommendationService (RecommendationRepository repository, EtfRepository etfRepository,AlpacaMarketDataClient client){
        this.repository = repository;
        this.etfRepository=etfRepository;
        this.client= client;
    }

    public List<Recommendation> findAll(){return this.repository.findAll();}

    public List<Recommendation> findByEtfId(Long etfId){return this.repository.findByEtfId(etfId);}

    public Recommendation create(Long etfId, RecommendationRequest recommendationRequest){
        return this.etfRepository.findById(etfId).map(etf -> {
            Recommendation recommendation = RecommendationMapper.toEntity(recommendationRequest,etf);
            return this.repository.save(recommendation);
        })
                .orElseThrow(()-> new EtfNotFoundException(etfId));
    }
    @Transactional
    public Recommendation generate(Long etfId){

        String reasoning;

        var etf = this.etfRepository.findById(etfId)
                .orElseThrow(()->new EtfNotFoundException(etfId));
        var marketdata = this.client.fetchSnapshot(etf.getTicker());

        double range = etf.getHigh52Week() - etf.getLow52Week();

        double thrid= range/3;
        double currentPrice = marketdata.latestTrade().p();
        int sellScore =0;
        int buyScore =0;


        if(currentPrice< etf.getLow52Week()+thrid){
            buyScore+=1;
        }
        if (currentPrice>etf.getHigh52Week()-thrid) {
            sellScore+=1;
        }
        if(currentPrice>marketdata.dailyBar().o()+(marketdata.dailyBar().o()/100)){
            sellScore+=1;
        }
        if(currentPrice<marketdata.dailyBar().o()-(marketdata.dailyBar().o()/100)){
            buyScore+=1;
        }

        int diff = buyScore - sellScore;
        if(diff > 0){
            reasoning = "Buy signal based on scoring: price near 52-week low and/or below daily open. Score:  "+diff;
            var recommendation = create(etfId, new RecommendationRequest(etfId, Signal.BUY, reasoning, currentPrice));
            recommendation.setScore(diff);
            return repository.save(recommendation);
        } else if (diff < 0) {
            reasoning = "Sell signal based on scoring: price near 52-week high and/or above daily open. Score:  "+ diff;
            var recommendation = create(etfId, new RecommendationRequest(etfId, Signal.SELL, reasoning, currentPrice));
            recommendation.setScore(diff);
            return repository.save(recommendation);
        }
        else  {
            reasoning = "Hold signal: no clear direction from scoring indicators" ;
            var recommendation = create(etfId, new RecommendationRequest(etfId, Signal.HOLD, reasoning, currentPrice));
            recommendation.setScore(diff);
            return repository.save(recommendation);
        }



    }


}
