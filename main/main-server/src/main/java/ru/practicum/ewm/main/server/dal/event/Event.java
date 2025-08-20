package ru.practicum.ewm.main.server.dal.event;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.ewm.main.server.dal.category.Category;
import ru.practicum.ewm.main.server.dal.location.Location;
import ru.practicum.ewm.main.server.dal.request.Request;
import ru.practicum.ewm.main.server.dal.user.User;

import java.time.Instant;
import java.util.List;

@Entity
@Table
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "eventId")
    private List<Request> requestList;

    @Column(name = "state_action_id")
    private StateAction stateAction;

    @Column
    private String annotation;

    @Column
    private String description;

    @Column
    private String title;

    @Column(name = "event_date")
    private Instant eventDate;

    @Column
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "published_on")
    private Instant publishedOn;

}
