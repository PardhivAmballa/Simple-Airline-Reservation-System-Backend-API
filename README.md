# Airline-Reservation-System-Backend-API
**Project Overview**
A simple Java Spring Boot backend for an Airline Reservation System.
Supports user login, searching flights, booking seats, and admin management.

**Tech Stack**
Java 21
Spring Boot

**Main API Endpoints**
1. Register User
   POST /api/users/register
   Role: PUBLIC
   Body: { "username": "", "password": "" }

2. Register Admin
   POST /api/users/admin/register
   Role: ADMIN
   Body: { "username": "", "password": "" }

3. Get All Flights
   GET /api/flights
   Role: USER, ADMIN

4. Search Flights
   GET /api/flights/search?source=X&destination=Y
   Role: USER, ADMIN

5. Get Flight By ID
   GET /api/flights/{flightId}
   Role: USER, ADMIN

6. Add Flight
   POST /api/flights
   Role: ADMIN
   Body: flight object

7. Update Flight Status
   PUT /api/flights/{flightId}/status
   Role: ADMIN
   Body: { "status": "", "currentLocation": "" }

8. Delete Flight
   DELETE /api/flights/{flightId}
   Role: ADMIN

9. Book Flight
   POST /api/bookings/{flightId}
   Role: USER

10. Get My Bookings
    GET /api/bookings/my-bookings
    Role: USER

11. Get All Bookings (Admin)
    GET /api/bookings/all
    Role: ADMIN

12. Admin Cancel Any Booking
    DELETE /api/bookings/{bookingId}
    Role: ADMIN

13. User - Request Cancellation
    POST /api/bookings/cancel-request/{bookingId}
    Role: USER

14. Admin - View All Cancellation Requests
    GET /api/bookings/admin/cancel-requests
    Role: ADMIN

15. Admin - Cancel ALL Requested
    POST /api/bookings/admin/cancel-all-requests
    Role: ADMIN

16. Admin - Cancel ONE Requested Booking (MISSING ENDPOINT ADDED)
    POST /api/bookings/admin/cancel-request/{bookingId}
    Role: ADMIN

**How To Run**
To run the project, open it in IntelliJ and run the
SimpleAirlineReservationSystemApplication.java file.

**Contributors & Responsibilities**
Team Name: Java Wizards
Team Members:
        1. Amballa Pardhiv (BT2024071)
        2. Thummala Hemanth Reddy (BT2024105)
        3. Chevuru V R Dinesh Karthik (BT20240199)
        4. Parimi Venkata Krishna (BT2024161)
        5. Pidela Yashwanth Reddy (BT2024103)
        6. Penumaka Sai Pramod (BT2024145)

Team Contribution:
        1. Pardhiv and Hemanth worked on the configuration and service components of the project.
        2. Dinesh and Yashwanth implemented the controller, exception handling, and model layers.
        3. Krishna and Pramod contributed to the persistence layer and database-related implementation.

