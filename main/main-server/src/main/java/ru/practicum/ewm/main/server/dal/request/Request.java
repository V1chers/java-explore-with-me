package ru.practicum.ewm.main.server.dal.request;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table
@Data
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int userId;

    @Column
    private int eventId;

    @Column(name = "request_status_id")
    private RequestStatus status;

    @Column
    private Instant created;
}
