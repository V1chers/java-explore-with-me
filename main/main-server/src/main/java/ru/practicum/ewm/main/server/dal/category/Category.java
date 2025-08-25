package ru.practicum.ewm.main.server.dal.category;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;
}
