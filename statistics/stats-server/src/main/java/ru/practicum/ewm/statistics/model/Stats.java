package ru.practicum.ewm.statistics.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "statistics")
@Data
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "app_id")
    private App app;

    @Column
    private String uri;

    @Column
    private String ip;

    @Column
    private Instant created;
}
