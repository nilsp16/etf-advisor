package de.dhbwravensburg.etfadvisor.service;

import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.exceptions.EtfNotFoundException;
import de.dhbwravensburg.etfadvisor.repository.EtfRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtfService {
    private final EtfRepository repository;

    public EtfService(EtfRepository repository) {
        this.repository = repository;
    }

    public List<Etf> findAll(){
        return this.repository.findAll();
    }

   public List<Etf> findByDividendPolicy(String dividendPolicy){
        return this.repository.findByDividendPolicyIgnoreCase(dividendPolicy);
   }

   public Etf findById(Long id){
            return this.repository.findById(id).orElseThrow(() -> new EtfNotFoundException(id));
   }
   public Etf findByTicker(String ticker){
            return this.repository.findByTickerIgnoreCase(ticker).orElseThrow(() -> new EtfNotFoundException(ticker));
   }
   public Etf findByIsin(String isin){
            return this.repository.findByIsin(isin).orElseThrow(() -> new EtfNotFoundException(isin));
   }

   public Etf create(Etf entity) {
        return this.repository.save(entity);
   }

   public Etf update(Long id, Etf updated){
        return this.repository.findById(id).map(existing -> {
                    existing.setName(updated.getName());
                    existing.setCurrency(updated.getCurrency());
                    existing.setIsin(updated.getIsin());
                    existing.setCurrentPrice(updated.getCurrentPrice());
                    existing.setDividendPolicy(updated.getDividendPolicy());
                    existing.setHigh52Week(updated.getHigh52Week());
                    existing.setLow52Week(updated.getLow52Week());
                    existing.setMarketCap(updated.getMarketCap());
                    existing.setReplicationMethod(updated.getReplicationMethod());
                    existing.setTer(updated.getTer());
                    existing.setTicker(updated.getTicker());

                    return repository.save(existing);
                }
                )
                .orElseThrow(() -> new EtfNotFoundException(id));

   }

   public boolean delete(Long id){
        if(!repository.existsById(id)){
            throw new EtfNotFoundException(id);
        }
        repository.deleteById(id);
        return true;

   }

}
