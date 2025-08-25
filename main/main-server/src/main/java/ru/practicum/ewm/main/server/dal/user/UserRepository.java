package ru.practicum.ewm.main.server.dal.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findByIdIn(List<Integer> idList, Pageable pageable);

    Optional<User> findByEmail(String email);

}
