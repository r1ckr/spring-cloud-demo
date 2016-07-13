#Reservation Client
A Reservation Service Client. 

This service is gonna be an edge service, something is gonna sit on the edge of the Datacenter and is gonna be exposed to other services like Handsets, Web Pages, etc.

This component is acting as a proxy, it gets the config from the Config Server, then registers with eureka and now it can call to reservation-service using this one like:

http://localhost:8080/reservation-service/reservations

This will return us the list of reservations but keep a close look at the URLs, all of them point to this "client" instead of the "service"