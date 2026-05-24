package de.dhbwravensburg.etfadvisor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Etf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String isin;
    private String ticker;
    private String currency;
    private double ter;     //Total Expense Ratio in %
    private double currentPrice;
    private String dividendPolicy;
    private double low52Week;
    private double high52Week;
    private double marketCap;
    private String replicationMethod;
    private String signal;


    @OneToMany(mappedBy = "etf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendations;
    @OneToMany(mappedBy = "etf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchlistEntry> watchlistEntries;

    public void addRecommendation(Recommendation recommendation){
        this.recommendations.add(recommendation);
        recommendation.setEtf(this);
    }
    public void removeRecommendation(Recommendation recommendation){
        this.recommendations.remove(recommendation);
        this.recommendations.remove(recommendation);
    }
    public void addWatchlistEntry(WatchlistEntry watchlistEntry){
        this.watchlistEntries.add(watchlistEntry);
        watchlistEntry.setEtf(this);
    }
    public void removeWatchlistEntry(WatchlistEntry watchlistEntry){
        this.watchlistEntries.remove(watchlistEntry);
    }

    //Constructor for testdata
    public Etf(Long id, String name, String isin, String ticker, String currency, double ter, double currentPrice, String dividendPolicy, double low52Week, double high52Week, double marketCap, String replicationMethod, String signal) {
        this.id = id;
        this.name = name;
        this.isin = isin;
        this.ticker = ticker;
        this.currency = currency;
        this.ter = ter;
        this.currentPrice = currentPrice;
        this.dividendPolicy = dividendPolicy;
        this.low52Week = low52Week;
        this.high52Week = high52Week;
        this.marketCap = marketCap;
        this.replicationMethod = replicationMethod;
        this.signal = signal;
    }
}
