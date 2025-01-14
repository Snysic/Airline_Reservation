CREATE TABLE airports (
    airport_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    airport_name VARCHAR(50) NOT NULL
);

CREATE TABLE airlines (
    airline_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    airline_name VARCHAR(50) NOT NULL
);

CREATE TABLE airplanes (
    airplane_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    airplane_name VARCHAR(50) NOT NULL,
    airline_id INT NOT NULL,
    FOREIGN KEY (airline_id) REFERENCES airlines (airline_id)
);

CREATE TABLE sources (
    source_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_time DATETIME NOT NULL,
    airport_id INT NOT NULL,
    FOREIGN KEY (airport_id) REFERENCES airports (airport_id)
);

CREATE TABLE destinations (
    destination_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_time DATETIME NOT NULL,
    airport_id INT NOT NULL,
    FOREIGN KEY (airport_id) REFERENCES airports (airport_id)
);

CREATE TABLE customers (
    customer_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE flights (
    flight_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    flight_code VARCHAR(10) NOT NULL,
    source_id INT NOT NULL,
    destination_id INT NOT NULL,
    airplane_id INT NOT NULL,
    fare DECIMAL(10, 2) NOT NULL,
    capacity INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (source_id) REFERENCES sources (source_id),
    FOREIGN KEY (destination_id) REFERENCES destinations (destination_id),
    FOREIGN KEY (airplane_id) REFERENCES airplanes (airplane_id)
);

CREATE TABLE customers_flights (
    customer_id INT NOT NULL,
    flight_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    FOREIGN KEY (flight_id) REFERENCES flights (flight_id)
);

CREATE TABLE reservations (
    reservation_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    customer_id INT NOT NULL,
    flight_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    FOREIGN KEY (flight_id) REFERENCES flights (flight_id)
);