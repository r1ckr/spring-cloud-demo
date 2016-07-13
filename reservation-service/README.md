#Reservation Service
A Spring Cloud Config client demo

##Ways of refreshing the application properties:

1. Calling the actuator endpoint refresh on the node itself
curl -i -X POST <host>:<port>/refresh
curl -i -X POST localhost:8000/refresh

This will return a list of the configs changed 

HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
X-Application-Context: reservation-service:8000
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 29 Apr 2016 13:12:28 GMT

["message"]

2. Another option is using Spring Cloud MTP Bus to connect multiple nodes through Rabbit MQ
For this we need to add the Eureka dependency and add the annotation @EnableDiscoveryClient in the Application.class (main)


In any case they will refresh the values of the elements annotated with @RefreshScope
http://projects.spring.io/spring-cloud/spring-cloud.html#_refresh_scope

Endpoints:
curl -i -X GET http://localhost:8000/message
This returns the property set in the config repo (message)

curl -i -X GET http://localhost:8000/reservations
This will return a list of reservations

curl -i -X GET http://localhost:8000/reservations/search/by-name?rn=Richard
Search a reservation called Richard