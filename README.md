# Spring Cloud Demo

## Components:
### Config Server:
This one is an independent component which reads from Git and provides configurations to the components registering with it

### Eureka Service
This another independent component providing service discovery where all the components will be registering, even himself

### Reservation Service
This is the backend demo service which consumes the Config Server, registers with eureka, etc

### Reservation Client
An edge service which connects to Reservation Service, circuit breaking, etc

## Installation
1. Run an istallation of RabbitMQ
    cd <rabitMQ files>/sbin
    ./rabbitmq-server

2. Run the Config Server, make sure is pointing to a valid git repo
    Up to here we have read from the git repo and the configs are ready to be provided

3. Run Eureka
    This will be the first Config Server client, if this one runs OK means that the config server is serving well
    Check it has started correctly and visit http://localhost:8761

4. Run Reservation Service
    When runing this one we've read the config from ConfigServer, registered with Eureka and connected to RabitMQ
    After boot:
    - Check it appears in the eureka web interface
    - Test it works with:
    
        curl -i -X GET http://localhost:8000/message
        This returns the property set in the config repo (message)
        
        curl -i -X GET http://localhost:8000/reservations
        This will return a list of reservations

5. Run Reservation Client
    When running this one we will have full 


## Sprign Cloud Features:
### Proxying
This feature can be used on the services that sits on the edge of the Datacenter, in this case reservation-client

Add to Application.class of reservation-client
@EnableZuulProxy

What this basically does is forwarding all the calls to the service we want to call, eg:
http://localhost:8050/reservation-service/reservations

What is happening is that reservation-client will go to eureka and look for a service called reservation-service and it will call to the resource /reservations


### Messaging for writes:
Here reservation-client can be configured to talk to reservation-service through messages for writes, the way it has to be configured is:
#### Reservation Client:
reservation-client.properties:
    spring.cloud.stream.bindings.output.destination=reservations
Reservation Client Application.class:
    @EnableBinding(Source.class)
Controller:
    @Output(Source.OUTPUT)
    @Autowired
    private MessageChannel messageChannel;

    @RequestMapping(method = RequestMethod.POST)
    public void write (@RequestBody Reservation reservation){
        this.messageChannel.send(MessageBuilder.withPayload(reservation.getReservationName()).build());
    }

#### Reservation Service
reservation-service.properties
    spring.cloud.stream.bindings.input.destination=reservations
Reservation Service Application.class:
    @EnableBinding(Sink.class)
Create class MessageReservationReceiver

### Cloud Bus
This is the way configurations will be updated from the config-server to all the clients consuming it

We would need in every component a new dependency:
<!--Spring Cloud Bus-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>

And now we can call to the config-server with the below curl and it will update all the components with the latest configuration
curl -i -X POST http://localhost:8888/bus/refresh

###Circuit Breakers:
These circuit breakers can be applied on the edge services (reservation-client) with Hystrix.

Application.class:
@EnableCircuitBreaker

In the Controller where reservation-client calls reservation-service add
@HystrixCommand(fallbackMethod = "getReservationNamesFallback")

Where getReservationNamesFallback is a method that returns an empty list.

To monitor the circuit breaker status there's a url:

http://localhost:8050/hystrix.stream

###Circuit Breaker Monitoring
This is another separate component (hystrix-dashboard) and it only needs:

bootstrap.properties:
spring.application.name=hystrix-dashboard
spring.cloud.config.uri=http://localhost:8888

Application.class:
@EnableHystrixDashboard

With that we can start that component and access to the dashboard:
http://localhost:8010/hystrix.html

#### Single
To visualize a single instance we just need to paste the url of the host we want to monitor, eg:
localhost:8050/hystrix.stream

#### Cluster



### Tracing
This is a distributed tracing system and it's done with Zipkin https://github.com/openzipkin/zipkin

Basically every component will be sending a trace to zipkin and a header to the next, by that way we can have a visual representation of every call. This is done adding:

pom.xml:
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zipkin</artifactId>
    </dependency>

In Application.class add the bean:

    @Bean
    AlwaysSampler alwaysSampler(){
        return new AlwaysSampler();
    }

Now we can run Zipkin and access the web interface, to run the Zipkin Server visit this website and follow the instructions:
http://zipkin.io/pages/quickstart

Basically is:
wget -O zipkin.jar 'https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec'
java -jar zipkin.jar

Now we can access the web interface and check the traces:
http://http://localhost:9411/

# References:
https://spring.io/blog/2014/06/03/introducing-spring-cloud
https://spring.io/blog/2015/01/20/microservice-registration-and-discovery-with-spring-cloud-and-netflix-s-eureka
http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html
https://www.youtube.com/watch?v=hV5TTSiFhRs
