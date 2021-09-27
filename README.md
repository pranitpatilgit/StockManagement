# StockManagement

This is a Spring Boot REST application for **Stock Management**.

### **Prerequisites**
- JDK 11+
- Maven


### **Build**
This project is maven project so can be build using following maven command

    mvn clean install

### **Deploy**
- Run com.pranitpatil.StockManagementApp.java from your IDE
  OR
- Run following command after maven build

        java -jar StockManagement-1.0-SNAPSHOT.jar

The Locking Duration can be set and altered using application.lockingInterval property


### **API Information**
API can be accessed from following swagger url
http://localhost:8090/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/

Following endpoints can be used

####    Stock Endpoints
- ####  Get All Stocks
    - Gets all stocks.
    - This is a API with pagination and sorting,
    - Provide page details as query parameters.

      Sample Request

             curl --location --request GET http://localhost:8090/api/stocks?page=0&size=3 

      Successful Response (With HTTP Status Code - 200)

           {
            "entity": [
                       {
                         "id": 1,
                         "name": "Google",
                         "price": 10.0
                       },
                       {
                          "id": 2,
                          "name": "Facebook",
                          "price": 20.0
                       },
                       {
                          "id": 3,
                          "name": "Confluent",
                          "price": 30.0
                       }
                      ],
            "pageNumber": 0,
            "totalPages": 2,
            "totalItems": 5
          }  
   
 
- ####  Get stock
  Gets the details of the stock

  Sample Request

        curl --location --request GET 'http://localhost:8090/api/stocks/{stockId}'

  Successful Response with HTTP Status code 200

        {
          "id": 1,
          "name": "Google",
          "price": 10.0
        }

  Error Response
  For invalid stock id the API will result in HTTP Status 400 with a valid error message.

- #### Add new stock
  Creates a new stock and returns it.

  Sample Request

          curl --header "Content-Type: application/json" \
            --request POST \
            --data '{"name": "DummyStock","price" : 12.24}' \
            http://localhost:8090/api/stocks

  Successful Response (With HTTP Status Code - 201)

          {
            "id": 6,
            "name": "DummyStock",
            "price": 12.24
          }

  In this API name is mandatory and price should be positive value.
  Any error in this will result in HTTP Status 400 with respective error message.


- ####  Update Stock
  Updates an existing stock

  Sample Request

               curl --header "Content-Type: application/json" \
                 --request PUT \
                 --data '{"id" : 6,"name": "Netflix","price": 11.0}' \
                 http://localhost:8090/api/stocks

  Successful Response (With HTTP Status Code - 200)

           {
             "id" : 6,
             "name": "Netflix",
             "price": 11.0
           }
  
  If the stock for update is last updated before 5 min (configurable duration) then and error occurs stating" This stock is locked for updates." along with HTTP Status code 423

- ####  Delete Stock
  Deletes the Stock

  Sample Request

        curl --location --request DELETE 'http://localhost:8090/api/stocks/{stockId}'

  Successful response is empty with Http Status Code 204 No Content

  If the stock for update is last updated before 5 min (configurable duration) then and error occurs stating" This stock is locked for updates." along with HTTP Status code 423


### **Future Improvements**

- Filters on GetAllStocks API
- User Interface
- Transaction Management


### **Author**
##### **Pranit Patil**