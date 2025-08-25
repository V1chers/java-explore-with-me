package ru.practicum.ewm.main.server.dal.compialtion;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.ewm.main.server.dal.event.Event;

import java.util.Set;

@Entity
@Table
@Data
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Boolean pinned;

    @Column
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_compilation",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<Event> eventList;
}
