package com.drones.io.repository;

import com.drones.io.entity.Drone;
import com.drones.io.enums.DroneState;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDronesRepository extends CrudRepository<Drone, UUID> {

    @EntityGraph(attributePaths = {"medications"})
    Optional<Drone> findBySerialNumber(String serialNumber);

    @Query("SELECT d FROM Drone d LEFT OUTER JOIN d.medications m WHERE d.batteryRemaining >= :batteryThreshold AND (d.state = 'IDLE' OR d.state = 'LOADING') GROUP BY d HAVING COALESCE(SUM(m.weight), 0) < d.weightLimit")
    List<Drone> listDronesAvailableForLoading(Integer batteryThreshold);

    List<Drone> findAllByState(DroneState state);
}
