package ru.practicum.ewm.main.server.dal.compialtion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query("select c " +
            "from Compilation as c " +
            "left join fetch c.eventList as e " +
            "left join fetch e.user " +
            "left join fetch e.category " +
            "left join fetch e.location " +
            "left join fetch e.requestList " +
            "where (?1 = false OR c.pinned = ?1)")
    Page<Compilation> findAll(Boolean pinned, Pageable pageable);

}
