package ru.practicum.ewm.main.server.dal.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.server.dal.category.Category;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Page<Event> findByCategory(Category category, Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "left join fetch e.user as u " +
            "left join fetch e.category " +
            "left join fetch e.location " +
            "left join fetch e.requestList " +
            "where u.id = ?1")
    Page<Event> findByUserId(int userId, Pageable pageable);

    @Query(value = "select e " +
            "from Event as e " +
            "left join fetch e.user as u " +
            "left join fetch e.category as c " +
            "left join fetch e.location " +
            "left join fetch e.requestList " +
            "where (:users IS NULL OR u.id in :users) " +
            "AND (:states IS NULL OR e.stateAction in :states) " +
            "AND (:categories IS NULL OR c.id in :categories) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (COALESCE(:rangeEnd, NULL) IS NULL OR e.eventDate <= :rangeEnd) ")
    Page<Event> findAllFiltered(@Param("users") List<Integer> users,
                                @Param("states") List<Integer> states,
                                @Param("categories") List<Integer> categories,
                                @Param("rangeStart") Instant rangeStart,
                                @Param("rangeEnd") Instant rangeEnd,
                                Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "left join fetch e.user as u " +
            "left join fetch e.category as c " +
            "left join fetch e.location " +
            "left join fetch e.requestList " +
            "where e.id in ?1")
    List<Event> findAllByIdList(List<Integer> eventIdList);

    @Query(value = "select e.* " +
            "from event e " +
            "left join users u on e.user_id = u.id " +
            "left join category c on e.category_id = c.id " +
            "left join location l on e.location_id = l.id " +
            "left join event_compilation ec on e.id = ec.event_id " +
            "left join request r on e.id = r.event_id " +
            "where (COALESCE(:text, NULL) IS NULL OR (e.annotation ilike :text OR e.description ilike :text)) " +
            "AND e.state_action_id = 1 " +
            "AND (COALESCE(:categories, NULL) IS NULL OR c.id IN (:categories)) " +
            "AND (COALESCE(:paid, NULL) IS NULL OR e.paid = :paid) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL OR CAST(e.event_date AS timestamp) > CAST(:rangeStart AS timestamp)) " +
            "AND (COALESCE(:rangeEnd, NULL) IS NULL OR CAST(e.event_date AS timestamp) < CAST(:rangeEnd AS timestamp)) " +
            "AND (COALESCE(:onlyAvailable, FALSE) = false OR e.id in " +
            "(select e2.id " +
            "from event e2 " +
            "left join request r2 on e2.id = r2.event_id " +
            "where r2.request_status_id = 2 " +
            "OR r2.id IS NULL " +
            "group by e2.id " +
            "having COUNT(r2.id) < e2.participant_limit " +
            " OR (COUNT(r2.id) = 0 AND e2.participant_limit > 0) )) " +
            "order by e.event_date",
            nativeQuery = true)
    Page<Event> getAllFilteredSortedByDate(@Param("text") String text,
                                           @Param("categories") List<Integer> categories,
                                           @Param("paid") Boolean paid,
                                           @Param("rangeStart") String rangeStart,
                                           @Param("rangeEnd") String rangeEnd,
                                           @Param("onlyAvailable") Boolean onlyAvailable,
                                           Pageable pageable);

    @Query(value = "select e.* " +
            "from event e " +
            "left join users u on e.user_id = u.id " +
            "left join category c on e.category_id = c.id " +
            "left join location l on e.location_id = l.id " +
            "left join request r on e.id = r.event_id " +
            "where (COALESCE(:text, NULL) IS NULL OR (e.annotation ilike :text OR e.description ilike :text)) " +
            "AND e.state_action_id = 1 " +
            "AND (COALESCE(:categories, NULL) IS NULL OR c.id IN (:categories)) " +
            "AND (COALESCE(:paid, NULL) IS NULL OR e.paid = :paid) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL OR CAST(e.event_date AS timestamp) > CAST(:rangeStart AS timestamp)) " +
            "AND (COALESCE(:rangeEnd, NULL) IS NULL OR CAST(e.event_date AS timestamp) < CAST(:rangeEnd AS timestamp)) " +
            "AND (COALESCE(:onlyAvailable, FALSE) = false OR e.id in " +
            "(select e2.id " +
            "from event e2 " +
            "left join request r2 on e2.id = r2.event_id " +
            "where r2.request_status_id = 2 " +
            "OR r2.id IS NULL " +
            "group by e2.id " +
            "having COUNT(r2.id) < e2.participant_limit" +
            " OR COUNT(r2.id) = 0 )) ",
            nativeQuery = true)
    List<Event> getAllFiltered(@Param("text") String text,
                               @Param("categories") List<Integer> categories,
                               @Param("paid") Boolean paid,
                               @Param("rangeStart") String rangeStart,
                               @Param("rangeEnd") String rangeEnd,
                               @Param("onlyAvailable") Boolean onlyAvailable);

}
