# Hotel Reservation System

This project is a Hotel Reservation System developed using the Spring Framework. It allows users to search, book, and manage hotel rooms online, providing a seamless and secure experience. Additionally, it includes features such as API testing with Postman, interactive API documentation via Swagger, and optional frontend development using React or Angular.

## Key Components

### 1. Spring Boot
- Simplifies application configuration and deployment.
- Enables the creation of standalone applications ready for execution.

### 2. Spring MVC
- Implements the Model-View-Controller pattern for managing user interactions.
- Includes controllers for handling operations such as room search, booking, etc.

### 3. Spring Data
- Uses Spring Data JPA for interacting with the database.
- Maps Java entities to database tables to store room, reservation, and user information.

### 4. Spring Security
- Integrates Spring Security to ensure platform security.
- Manages user authentication and authorization for accessing different functionalities.

### 5. Postman
- Utilizes Postman for testing the developed APIs.
- Creates API collections and test scenarios to ensure proper application functionality.

### 6. Swagger (Optional)
- Configures Swagger for generating interactive API documentation.
- Allows users to easily view API documentation and test calls directly from the Swagger interface.

### 7. Frontend with React/Angular (Optional) (Planning to use Angular)
- Provides the option to develop the application frontend using React or Angular.
- Uses reusable components to build the user interface with features such as room search, booking, etc.

## Database
- Utilizes a relational database such as MySQL or PostgreSQL, I opted for PostgreSQL.
- Supported by Spring Data JPA, providing a suitable data structure for storing room, reservation, and user information.

## Main Features
1. **Room Search and Booking:**
   - Users can search for available hotel rooms by specifying criteria such as arrival date, departure date, number of people, etc.
   - Displays available rooms with details such as price, amenities, photos, etc.
   - Allows users to book a room by providing necessary information.

2. **Reservation Management:**
   - Users can view their reservation history and manage details such as dates, rooms, etc.
   - Administrators can view all bookings and manage their status.

3. **Authentication and User Management:**
   - Users can create an account, log in, and manage their personal information.
   - Administrators can manage user accounts and associated roles.

4. **Room Management (for Administrators):**
   - Administrators can add new rooms, modify existing room details, and delete obsolete rooms.
   - Includes availability management to update room availability dates.

## Deliverables

### 1. Report: (Pending)
- A concise report summarizing the implemented features.
- Includes screenshots illustrating the user interface and basic functionality of the application.

### 2. GitHub Repository Link: (Here it is!)
- A link to the GitHub repository containing the application source code.
- The repository is well-documented and logically organized to facilitate code understanding.

## Technologies Used

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- PostgreSQL (Database)
- Postman (API Testing)
- Swagger (Optional)
- Angular (Optional, for Frontend)

