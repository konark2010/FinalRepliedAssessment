This is a Spring Boot-based REST API that allows users to register, follow other users, manage follow requests, and block users. The API ensures blocked users cannot follow or interact with each other and also provides a mechanism for mutual friends to view additional information.

## Features

- **User Registration**: Allows users to register with a unique username and birthdate.
- **Follow User**: Allows users to send follow requests, with checks for blocking conditions.
- **Block User**: Allows users to block others and prevent further interactions.
- **Follow Requests**: Handles the status of follow requests (pending, accepted, denied).
- **View Followers/Following**: Allows users to view their followers and who they are following.
  
## API Endpoints

### 1. **User Registration**
- **POST** `/api/users/register`
  
  Registers a new user with a given username and birthdate.
  
  **Request Parameters**:
  - `username` (String) - The username of the user.
  - `birthdate` (String) - The birthdate of the user (format: `yyyy-MM-dd`).
  
  **Response**:
  - Status: `200 OK`
  - Body: A JSON object representing the newly registered user.

  Example Request:
  ```bash
  POST /api/users/register?username=johndoe&birthdate=1990-01-01
  ```

### 2. **Follow User**
- **POST** `/api/users/follow`
  
  Sends a follow request from one user to another. Checks if either user has blocked the other.
  
  **Request Parameters**:
  - `followerId` (UUID) - The ID of the user who wants to follow.
  - `followeeId` (UUID) - The ID of the user being followed.
  
  **Response**:
  - Status: `200 OK`
  - Body: `"Follow request sent."`
  
  Example Request:
  ```bash
  POST /api/users/follow?followerId=f7764433-8b08-47f0-b6c0-0e0ce9f17728&followeeId=ab54367a-d037-47dc-9721-0eb1e042498a
  ```

### 3. **View All Users**
- **GET** `/api/users`
  
  Retrieves a list of all registered users.
  
  **Response**:
  - Status: `200 OK`
  - Body: A list of all users.

  Example Request:
  ```bash
  GET /api/users
  ```

### 4. **View Followers**
- **GET** `/api/users/{userId}/followers`
  
  Retrieves a list of followers for a specific user.
  
  **Request Parameters**:
  - `userId` (UUID) - The ID of the user whose followers are being fetched.
  
  **Response**:
  - Status: `200 OK`
  - Body: A list of followers (user data).

  Example Request:
  ```bash
  GET /api/users/ab54367a-d037-47dc-9721-0eb1e042498a/followers
  ```

### 5. **View Following**
- **GET** `/api/users/{userId}/following`
  
  Retrieves a list of users that a specific user is following.
  
  **Request Parameters**:
  - `userId` (UUID) - The ID of the user whose following list is being fetched.
  
  **Response**:
  - Status: `200 OK`
  - Body: A list of users being followed (user data).

  Example Request:
  ```bash
  GET /api/users/ab54367a-d037-47dc-9721-0eb1e042498a/following
  ```

### 6. **Block User**
- **POST** `/api/users/block`
  
  Blocks a user, preventing any further interactions between the two users.
  
  **Request Parameters**:
  - `userId` (UUID) - The ID of the user who is blocking.
  - `blockedUserId` (UUID) - The ID of the user being blocked.
  
  **Response**:
  - Status: `200 OK`
  - Body: `"User blocked."`
  
  Example Request:
  ```bash
  POST /api/users/block?userId=f7764433-8b08-47f0-b6c0-0e0ce9f17728&blockedUserId=ab54367a-d037-47dc-9721-0eb1e042498a
  ```

### 7. **Accept Follow Request**
- **POST** `/api/users/requests/accept`
  
  Accepts a pending follow request.
  
  **Request Parameters**:
  - `requestId` (UUID) - The ID of the follow request.
  
  **Response**:
  - Status: `200 OK`
  - Body: `"Follow request accepted."`
  
  Example Request:
  ```bash
  POST /api/users/requests/accept?requestId=f7764433-8b08-47f0-b6c0-0e0ce9f17728
  ```

### 8. **Deny Follow Request**
- **POST** `/api/users/requests/deny`
  
  Denies a pending follow request.
  
  **Request Parameters**:
  - `requestId` (UUID) - The ID of the follow request.
  
  **Response**:
  - Status: `200 OK`
  - Body: `"Follow request denied."`
  
  Example Request:
  ```bash
  POST /api/users/requests/deny?requestId=f7764433-8b08-47f0-b6c0-0e0ce9f17728
  ```

### 9. **View Follow Requests**
- **GET** `/api/users/requests`
  
  Retrieves the list of pending follow requests for a specific user.
  
  **Request Parameters**:
  - `userId` (UUID) - The ID of the user whose follow requests are being fetched.
  
  **Response**:
  - Status: `200 OK`
  - Body: A list of follow requests (request data).

  Example Request:
  ```bash
  GET /api/users/requests?userId=f7764433-8b08-47f0-b6c0-0e0ce9f17728
  ```

## Technologies Used

- **Java**: Programming language used for backend development.
- **Spring Boot**: Framework for building the API.
- **H2 Database**: In-memory database used for development and testing.
- **Gradle**: Dependency management and build tool.

## Running the project

### 1. **Run the Application**:
```bash
./gradlew bootRun  
```

The application will start on `http://localhost:8081`.

### 2. **Test the Application with test cases**:
```bash
./gradlew test  
```

### 3. **Testing the API**:
You can use **Postman** or **curl** to test the API endpoints.



---

This README provides an overview of the Replied Follow & Block API, including all the relevant endpoints.
