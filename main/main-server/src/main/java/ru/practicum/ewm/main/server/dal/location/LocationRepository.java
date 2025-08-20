package ru.practicum.ewm.main.server.dal.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findByLatitudeAndLongitude(float latitude, float longitude);
}
