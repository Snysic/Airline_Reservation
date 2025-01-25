
INSERT INTO roles (id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER')
ON DUPLICATE KEY UPDATE 
    name = VALUES(name);


INSERT INTO users (id, username, password, email) VALUES
(1, 'admin', '$2a$12$rfqMWrD3ldjkpru6P2VdgeXWN4mLYq3fYtoFM6PB4OjvHUzTHWswW', 'admin@example.com'),
(2, 'user', '$2a$12$0X9ToDpvs3CDXSY0pucrMOKZ9SRmJGoR8loAeqM8./WStiv90iQIG', 'user@example.com')
ON DUPLICATE KEY UPDATE 
    username = VALUES(username),
    password = VALUES(password),
    email = VALUES(email);


INSERT INTO roles_users (role_id, user_id) VALUES
(1, 1), 
(2, 2) 
ON DUPLICATE KEY UPDATE 
    role_id = VALUES(role_id),
    user_id = VALUES(user_id);


INSERT INTO flights (id, flight_code, source, destination, departure_time, arrival_time, available_seats, status) VALUES
(1, 'FL123', 'New York', 'Los Angeles', '2025-01-17 08:00:00', '2025-01-17 11:00:00', 100, 'AVAILABLE'),
(2, 'FL456', 'Chicago', 'Miami', '2025-01-18 14:00:00', '2025-01-18 18:00:00', 150, 'AVAILABLE')
ON DUPLICATE KEY UPDATE 
    flight_code = VALUES(flight_code),
    source = VALUES(source),
    destination = VALUES(destination),
    departure_time = VALUES(departure_time),
    arrival_time = VALUES(arrival_time),
    available_seats = VALUES(available_seats),
    status = VALUES(status);


INSERT INTO reservations (id, flight_id, user_id, reservation_time, reserved_seats, status) VALUES
(1, 1, 2, '2025-01-15 10:00:00', 2, 'CONFIRMED'),
(2, 2, 2, '2025-01-15 11:00:00', 1, 'PENDING')
ON DUPLICATE KEY UPDATE 
    flight_id = VALUES(flight_id),
    user_id = VALUES(user_id),
    reservation_time = VALUES(reservation_time),
    reserved_seats = VALUES(reserved_seats),
    status = VALUES(status);