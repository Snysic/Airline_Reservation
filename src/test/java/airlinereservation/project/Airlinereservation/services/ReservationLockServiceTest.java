package airlinereservation.project.Airlinereservation.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationLockServiceTest {

    private ReservationLockService reservationLockService;

    @BeforeEach
    void setUp() {
        reservationLockService = new ReservationLockService();
    }

    @Test
    void testLockSeats_Success() {
        Long flightId = 1L;
        reservationLockService.lockSeats(flightId);
        assertTrue(reservationLockService.isSeatLocked(flightId), "Seats should be locked after locking.");
    }

    @Test
    void testIsSeatLocked_InitiallyFalse() {
        Long flightId = 2L;
        assertFalse(reservationLockService.isSeatLocked(flightId), "Seats should not be locked initially.");
    }

    @Test
    void testReleaseExpiredLocks() {
        Long flightId = 3L;
        reservationLockService.lockSeats(flightId);

        assertTrue(reservationLockService.isSeatLocked(flightId), "Seats should be locked initially.");

        manuallyExpireLock(flightId);

        reservationLockService.releaseExpiredLocks();

        assertFalse(reservationLockService.isSeatLocked(flightId), "Seats should be unlocked after expiration.");
    }

    @Test
    void testSeatRemainsLockedBeforeExpiration() {
        Long flightId = 4L;
        reservationLockService.lockSeats(flightId);

        assertTrue(reservationLockService.isSeatLocked(flightId), "Seats should be locked.");

        reservationLockService.releaseExpiredLocks();

        assertTrue(reservationLockService.isSeatLocked(flightId), "Seats should still be locked.");
    }

    private void manuallyExpireLock(Long flightId) {
        try {
            var field = ReservationLockService.class.getDeclaredField("lockedSeats");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            var lockedSeats = (java.util.Map<Long, LocalDateTime>) field.get(reservationLockService);
            lockedSeats.put(flightId, LocalDateTime.now().minusMinutes(16));
        } catch (Exception e) {
            throw new RuntimeException("Failed to modify lockedSeats", e);
        }
    }
}
