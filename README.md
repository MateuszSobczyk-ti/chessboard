# Chessboard Play API

API for simulating chessboard fights.

## Description

This API allows you to simulate battles on a chessboard, including:

* Starting a new game with chessboard and unit configurations.
* Executing commands for units (move, shoot).
* Retrieving a list of units for a given player.
* Executing random commands for units.

## Endpoints

### Start a New Game

* **POST** `/api/v1/chessboard/newGame`
* **Description:** Set the game configuration: chessboard dimensions and units for each player. If `oldGameId` is set, the old game will be archived. The response contains the new game ID, game start date, and unit positions on the chessboard.
* **Parameters:**
    * `oldGameId` (query, integer, optional)
* **Request Body:**
   ```json
    {
      "width": 5,
      "height": 5,
      "whiteUnits": {
        "archer": 2,
        "transport": 1,
        "cannon": 1
      },
      "blackUnits": {
        "archer": 2,
        "transport": 1,
        "cannon": 1
      }
    }
    ```
* **Response:**
   * 200 OK
    ```json
    {
      "gameId": 1,
      "start": "2024-01-01T12:00:00Z",
      "units": [
        {
          "id": 1,
          "player": "WHITE",
          "unitType": "ARCHER",
          "widthPosition": 0,
          "heightPosition": 0,
          "destroyed": false,
          "commandCounter": 0
        },
        // ... more units ...
      ]
    }
    ```
    * 400 Bad Request  
   _TooManyUnitsException_
   ```json
      {
         "statusCode": 400,
         "message": "Too many units for configured chessboard dimension!"
      }
   ```
   ```
      {
         "width": "Width dimension cannot be empty",
      }
   ```
   ```
      {
         "height": "dimension value must be greater than 1"
      }
   ```

### Unit action

* **POST** `/api/v1/chessboard/unit/action`
* **Description:** Issue a command (move or shoot) to a unit. Specify the target position. The response contains the action result message and the new position for the moved unit or destroyed unit details.
* **Request Body:**
    ```json
    {
      "gameId": 1,
      "unitId": 1,
      "player": "WHITE",
      "actionType": "MOVE",
      "firstCoordinateDto": {
        "direction": "UP",
        "fields": 1
      },
      "secondCoordinateDto": null
    }
    ```
* **Response:**
   * 200 OK
    ```json
    {
      "message": "Unit moved",
      "unitsResponse": {
        "id": 1,
        "player": "WHITE",
        "unitType": "ARCHER",
        "widthPosition": 0,
        "heightPosition": 1,
        "destroyed": false,
        "commandCounter": 1
      }
    }
    ```
    * 400 Bad Request  
  _ActionTimeIntervalException_
   ```json
   {
      "statusCode": 400,
      "message": "ARCHER cannot SHOOT yet!"
   }
   ```
   _UnitNotFoundException_
   ```json
   {
      "statusCode": 400,
      "message": "Unit with id -1 not found!"
   }
   ```
   _ActionTypeException_
   ```json
   {
      "statusCode": 400,
      "message": "Action SHOOT is not allowed for TRANSPORT unit!"
   }
   ```
   _MoveScopeExeception_
   ```json
   {
      "statusCode": 400,
      "message": "Too big move scope! For TRANSPORT allowed scope is 3"
   }
   ```
   _NewPositionOutOfChessboardException_
   ```json
   {
     "statusCode": 400,
     "message": "New unit position is out of chessboard dimension!"
   }
   ```
   _DiagonallyMoveException_
   ```json
   {
      "statusCode": 400,
      "message": "For ARCHER diagonally move is not allowed!"
   }
   ```

### Random Unit Command

* **POST** `/api/v1/chessboard/unit/{unitId}/random-command`
* **Description:** Issue a random command to a specified unit. The unit can shoot or move. The response contains the action result message and the new position for the moved unit or destroyed unit details.
* **Parameters:**
    * `unitId` (path, integer, required)
    * `gameId` (query, integer, required)
    * `player` (query, string, required, enum: `WHITE`, `BLACK`)
* **Response:**
    ```json
    {
      "message": "Unit shoot",
      "unitsResponse": {
        "id": 2,
        "player": "BLACK",
        "unitType": "ARCHER",
        "widthPosition": 2,
        "heightPosition": 2,
        "destroyed": true,
        "commandCounter": 0
      }
    }
    ```

### Retrieve units

* **GET** `/api/v1/chessboard/units`
* **Description:** Retrieve a list of units for a specified player. The response contains unit details: ID, type, position on the chessboard, status (destroyed or not), and command execution counter.
* **Parameters:**
    * `gameId` (query, integer, required)
    * `player` (query, string, required, enum: `WHITE`, `BLACK`)
* **Response:**
    ```json
    [
      {
        "id": 1,
        "player": "WHITE",
        "unitType": "ARCHER",
        "widthPosition": 0,
        "heightPosition": 0,
        "destroyed": false,
        "commandCounter": 0
      },
      // ... more units ...
    ]
    ```

## Getting Started

1. Clone the repository.
    ```bash
    git clone https://github.com/MateuszSobczyk-ti/chessboard.git
    cd chessboard
    ```
2. Run the Spring Boot application.
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
3. Test application running on:
    ```
    http://localhost:8080
    ```
4. To review documentation open Swagger UI at 
    ```
    http://localhost:8080/swagger-ui.html.
    ```

## Technology
* Java 21
* Spring Boot 3.2
* H2 Database
* Junit, Mockito
* Swagger
* Maven
