package de.dhbwravensburg.etfadvisor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Etf {

    private long id;
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

    public static List<Etf> generateTestData(){
        return List.of(
                new Etf(1L, "Vanguard FTSE All-World", "IE00B3RBWM25",
                        "VWRL", "USD", 0.22, 112.45,
                        "Distributing", 89.10, 115.30, 18.5, "Physical","sell"),

                new Etf(2L, "iShares Core MSCI World", "IE00B4L5Y983",
                        "IWDA", "USD", 0.20, 98.76,
                        "Accumulating", 78.40, 101.20, 62.3, "Physical","buy"),

                new Etf(3L, "Xtrackers MSCI Emerging Markets", "IE00BTJRMP35",
                        "XMME", "EUR", 0.18, 24.31,
                        "Accumulating", 19.80, 26.10, 4.1, "Physical","hold"),

                new Etf(4L, "iShares S&P 500", "IE0031442068",
                        "IUSA", "USD", 0.07, 45.67,
                        "Accumulating", 36.20, 47.90, 89.7, "Physical","buy"),

                new Etf(5L, "Amundi MSCI World", "LU1681043599",
                        "CW8", "EUR", 0.12, 412.30,
                        "Accumulating", 320.50, 425.80, 12.8, "Synthetic","sell")
        );
    }

}
