package airlinereservation.project.Airlinereservation.repositories;

import airlinereservation.project.Airlinereservation.models.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
}
