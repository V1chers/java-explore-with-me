package ru.practicum.ewm.main.server.dal.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByUserId(int userId);

    Request findByUserIdAndEventId(int userId, int eventId);

    List<Request> findAllByEventId(int eventId);

    List<Request> findAllByEventIdIn(Set<Integer> eventId);

    List<Request> findAllByEventIdAndIdIn(int eventId, List<Integer> requestIdList);

    List<Request> findAllByEventIdAndStatus(int eventId, RequestStatus statusId);
}
