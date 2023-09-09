package com.drones.io.repository;

import com.drones.io.model.Drone;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IDronesRepository extends CrudRepository<Drone, String> {

    @EntityGraph(attributePaths = {"medications"})
    Optional<Drone> findBySerialNumber(String serialNumber);

    @Query("SELECT d FROM Drone d JOIN d.medications m WHERE d.batteryRemaining >= :batteryThreshold AND d.state = 'LOADING' GROUP BY d HAVING sum(m.weight) < d.maximumWeightCapacity")
    List<Drone> listDronesAvailableForLoading(Integer batteryThreshold);
}
