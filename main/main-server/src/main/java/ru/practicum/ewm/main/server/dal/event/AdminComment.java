package ru.practicum.ewm.main.server.dal.event;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admin_comment")
@Data
public class AdminComment {

    @Id
    @Column(name = "event_id")
    private int tId;

    @Column
    private String Comment;

    @OneToOne
    @MapsId
    @JoinColumn(name = "event_id")
    private Event event;
}
