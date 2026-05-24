package de.dhbwravensburg.etfadvisor.mapper;

import de.dhbwravensburg.etfadvisor.dto.EtfRequest;
import de.dhbwravensburg.etfadvisor.dto.EtfResponse;
import de.dhbwravensburg.etfadvisor.entity.Etf;

public class EtfMapper {

    public static Etf toEntity(Long id, EtfRequest request){
        return new Etf(
                id,
                request.name(),
                request.isin(),
                request.ticker(),
                request.currency(),
                request.ter(),
                request.currentPrice(),
                request.dividendPolicy(),
                request.low52Week(),
                request.high52Week(),
                request.marketCap(),
                request.replicationMethod()
        );
    }
    public static EtfResponse toResponse(Etf etf){
       return new EtfResponse(

        etf.getId(),
        etf.getName(),
        etf.getIsin(),
        etf.getTicker(),
        etf.getCurrency(),
        etf.getTer(),
        etf.getCurrentPrice(),
        etf.getDividendPolicy(),
        etf.getLow52Week(),
        etf.getHigh52Week(),
        etf.getMarketCap(),
        etf.getReplicationMethod()
        );
    }
}
