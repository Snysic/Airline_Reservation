# Aerolinea Factoría F5

## ✅Project Description
The Aerolínea Factoría F5 project is a comprehensive RESTful API designed for managing an airline's operations, including user management, flight scheduling, reservation handling, and destination management. The system incorporates advanced features such as secure authentication using JWT or Basic Auth and ensures flight availability is accurately maintained. The project emphasizes clean architecture and object-oriented programming principles to deliver a robust and maintainable system.

The system guarantees that no bookings can be made for unavailable flights or those exceeding their operational dates.

## ✅Features Overview

# User Management
 - Role-Based Access Control: Includes ROLE_ADMIN and ROLE_USER for access differentiation.
 - User Registration: Allows users to sign up with personal information.
 - Profile Picture Upload: Users can upload a profile picture; a default image is shown otherwise.
 - User Login: Secure login with JWT or Basic Auth for API access.
# Flight Management
 - Flight Scheduling: Automatic creation of flight data during initialization via SQL scripts.
 - Dynamic Status Updates: Automatically marks flights as unavailable (false) when seats are fully booked or dates are invalid.
 - Search Functionality: Search flights by departure and arrival locations, date, and available seats.
# Reservation Management
 - Booking Validation: Ensures reservations can only be made for available flights.
 - Seat Locking: Locks seats for 15 minutes during booking to prevent double-booking.
 - User-Specific Reservations: Allows users to view their reservation history.
# Admin Management
 - CRUD for Airports: Manage airport data.
 - CRUD for Flight Routes: Add, update, delete, and view flight routes.
 - Reservation Overview: View a summary of reservations made by users.
# Exception Handling
 - Custom Exception Handling: Ensures a seamless user experience with detailed error messages.

## ✅Prerequisites

 - Java Development Kit (JDK) 21: Required to compile and run the project.
 - Maven: For dependency management and project build.
 - Database: MySQL or PostgreSQL for data storage.
 - IDE: A modern IDE like IntelliJ IDEA or Eclipse.
 - Docker (optional): For containerization.
  
## ✅Installation Steps

1 Clone the repository:

 ``https://github.com/Snysic/SpringbootAerolinea.git``

2 Set up the project in your IDE:

- Open the project in your preferred IDE.
- Ensure JDK 21 is configured.
- Verify the presence of the pom.xml file.
  
3 Configure the database:

 - For MySQL or PostgreSQL, update application.properties with the appropriate database URL, username, and password.

4 Run the application:

- Navigate to the SpringaerolineaApplication class.
- Run the project to start the server.
  
5 Access the application:

- Open your browser and navigate to http://localhost:8080.

## ✅API Endpoints

User Management
- Register a User:
POST http://localhost:8080/api/users/register

- Login:
POST http://localhost:8080/api/users/login

- Upload Profile Picture:
POST http://localhost:8080/api/users/profile/upload

- Get User Reservations:
GET http://localhost:8080/api/users/reservations

Flight Management
- Search Flights:
GET http://localhost:8080/api/flights/search

- Get Flight Details:
GET http://localhost:8080/api/flights/{id}

Reservation Management
- Create a Reservation:
POST http://localhost:8080/api/reservations

- Cancel a Reservation:
DELETE http://localhost:8080/api/reservations/{id}

Admin Management
- CRUD for Airports:
- POST http://localhost:8080/api/admin/airports
- PUT http://localhost:8080/api/admin/airports/{id}
- DELETE http://localhost:8080/api/admin/airports/{id}
- GET http://localhost:8080/api/admin/airports

- CRUD for Flight Routes:
- POST http://localhost:8080/api/admin/routes
- PUT http://localhost:8080/api/admin/routes/{id}
- DELETE http://localhost:8080/api/admin/routes/{id}
- GET http://localhost:8080/api/admin/routes

## ✅Testing

- Unit Tests: Ensure core functionalities are tested.
- Integration Tests: Validate end-to-end workflows.

Running Tests
Run the test suite in your IDE or use Maven:

mvn test
Review test coverage reports.

## ✅Technology Stack

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) 
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

## ✅Class Diagram

## ✅Database Schema

## ✅Author

Maria V [![Snysic](https://img.icons8.com/ios-glyphs/30/000000/github.png)](https://github.com/Snysic)


## ✅Disclaimer

This project is developed as part of the Factoría F5 bootcamp for educational purposes only. It is not intended for commercial use. All trademarks and third-party references belong to their respective owners.