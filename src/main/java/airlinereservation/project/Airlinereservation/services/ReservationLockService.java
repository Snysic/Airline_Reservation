package airlinereservation.project.Airlinereservation.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReservationLockService {

    private final Map<Long, LocalDateTime> lockedSeats = new ConcurrentHashMap<>();

    public void lockSeats(Long flightId) {
        lockedSeats.put(flightId, LocalDateTime.now().plusMinutes(15));
    }

    public boolean isSeatLocked(Long flightId) {
        return lockedSeats.containsKey(flightId) && lockedSeats.get(flightId).isAfter(LocalDateTime.now());
    }

    @Scheduled(fixedRate = 60000)
    public void releaseExpiredLocks() {
        LocalDateTime now = LocalDateTime.now();
        lockedSeats.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
}
