package de.dhbwravensburg.etfadvisor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etf_id")
    private Etf etf;

    @Enumerated(EnumType.STRING)
    private Signal signal;

    private String reasoning;

    private LocalDateTime generatedAt;
    private double priceAtGeneration;
}
