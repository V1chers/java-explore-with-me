package ru.practicum.ewm.main.server.dal.location;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private float latitude;

    @Column
    private float longitude;
}
