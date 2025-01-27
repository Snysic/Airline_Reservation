package airlinereservation.project.Airlinereservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByFlightId(Long flightId);

    List<Reservation> findByReservationTimeAfter(LocalDateTime reservationTime);

    List<Reservation> findByStatus(String status);

    List<Reservation> findByUserAndStatus(User user, String status);
}
