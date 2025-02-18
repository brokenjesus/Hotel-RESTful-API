# Hotel RESTful API

This is a RESTful API for managing hotels, providing operations for retrieving hotel details, creating new hotels, adding amenities, searching for hotels by various parameters, and generating statistical data in the form of histograms.

## Features

- **Get a List of All Hotels**: Retrieve brief information about all hotels.
- **Get Hotel Details**: Retrieve detailed information about a specific hotel.
- **Search Hotels**: Search for hotels based on various parameters like name, brand, city, county, and amenities.
- **Create a New Hotel**: Add a new hotel to the system.
- **Add Amenities to Hotels**: Add a list of amenities to a specific hotel.
- **Generate Histograms**: Get a histogram of hotels grouped by parameters like brand, city, county, and amenities.

## Running the Application

To run the application, use the following Maven command:

mvn spring-boot:run

The application will be available on port `8092`, and the base URL for all endpoints is `/property-view`.

## Endpoints

### 1. Get All Hotels

- **URL**: `/property-view/hotels`
- **Method**: `GET`
- **Response Example**:
    ```
    [
  
        {
  
            "id": 1,
  
            "name": "DoubleTree by Hilton Minsk",
  
            "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor...",
  
            "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
  
            "phone": "+375 17 309-80-00"
  
        }
  
    ]
    ```

### 2. Get Hotel Details by ID

- **URL**: `/property-view/hotels/{id}`
- **Method**: `GET`
- **Response Example**:
    ```
    {
  
        "id": 1,
  
        "name": "DoubleTree by Hilton Minsk",
  
        "brand": "Hilton",
  
        "address": {
  
            "houseNumber": 9,
  
            "street": "Pobediteley Avenue",
  
            "city": "Minsk",
  
            "county": "Belarus",
  
            "postCode": "220004"
  
        },
  
        "contacts": {
  
            "phone": "+375 17 309-80-00",
  
            "email": "doubletreeminsk.info@hilton.com"
  
        },
  
        "arrivalTime": {
  
            "checkIn": "14:00",
  
            "checkOut": "12:00"
  
        },
  
        "amenities": [
  
            "Free parking",
  
            "Free WiFi",

            "Non-smoking rooms",
  
            "Concierge",
  
            "On-site restaurant",
  
            "Fitness center",
  
            "Pet-friendly rooms",
  
            "Room service",
  
            "Business center",
  
            "Meeting rooms"
  
        ]
  
    }
    ```

### 3. Search Hotels by Parameters

- **URL**: `/property-view/search`
- **Method**: `GET`
- **Query Parameters**:

    - `name` (optional): Hotel name.
      
    - `brand` (optional): Hotel brand.
      
    - `city` (optional): City where the hotel is located.
      
    - `county` (optional): County where the hotel is located.
      
    - `amenities` (optional): List of amenities to filter by.
      
- **Example Query**: `/property-view/search?city=minsk`
- **Response Example**: Same format as `/property-view/hotels`.

### 4. Create a New Hotel

- **URL**: `/property-view/hotels`
- **Method**: `POST`
- **Request Body Example**:
    ```
    {
  
        "name": "DoubleTree by Hilton Minsk",
  
        "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor...",
  
        "brand": "Hilton",

        "address": {
  
            "houseNumber": 9,
  
            "street": "Pobediteley Avenue",
  
            "city": "Minsk",
  
            "county": "Belarus",
  
            "postCode": "220004"
  
        },
  
        "contacts": {
  
            "phone": "+375 17 309-80-00",
  
            "email": "doubletreeminsk.info@hilton.com"
  
        },
  
        "arrivalTime": {
  
            "checkIn": "14:00",
  
            "checkOut": "12:00"
  
        }
  
    }
    ```
- **Response Example**:
    ```
    {
  
        "id": 1,
  
        "name": "DoubleTree by Hilton Minsk",
  
        "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor...",
  
        "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
  
        "phone": "+375 17 309-80-00"
  
    }
    ```

### 5. Add Amenities to a Hotel

- **URL**: `/property-view/hotels/{id}/amenities`
- **Method**: `POST`
- **Request Body Example**:
    ```
    [
  
        "Free parking",
  
        "Free WiFi",
  
        "Non-smoking rooms",
  
        "Concierge",
  
        "On-site restaurant",
  
        "Fitness center",
  
        "Pet-friendly rooms",
  
        "Room service",
  
        "Business center",
  
        "Meeting rooms"
  
    ]
      ```
  
- **Response**: `201 Created`

### 6. Get Hotel Histogram Data

- **URL**: `/property-view/histogram/{param}`
- **Method**: `GET`
- **Parameters**: 
    - `param` (path parameter): Parameter for grouping the hotels. Can be `brand`, `city`, `county`, or `amenities`.
- **Example Query**: `/property-view/histogram/city`
- **Response Example**:
    ```
    {
  
        "Minsk": 1,
  
        "Moscow": 2
  
    }
    ```

    For amenities:
  ```
    {
  
        "Free parking": 1,
  
        "Free WiFi": 20,
  
        "Non-smoking rooms": 5
  
    }
  ```

## Technologies Used

- **Maven**
- **Java 17+**
- **Spring Boot**
- **Spring JPA**
- **Liquibase** for database migrations
- **H2 Database** (in-memory database for development)
- **Junit**
- **Mockito**

## Switching to MySQL

The application is configured to work with an H2 in-memory database by default, but it is easy to switch to MySQL by modifying the `application.properties` file. Use:

```spring.profiles.active=h2``` 

```spring.profiles.active=mysql```


## Swagger UI

The API has integrated Swagger UI for interactive documentation. You can access it at:

http://localhost:8092/swagger-ui/index.html
