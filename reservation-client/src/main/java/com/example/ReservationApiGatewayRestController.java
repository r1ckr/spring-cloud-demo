package com.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationApiGatewayRestController {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Output(Source.OUTPUT)
    @Autowired
    private MessageChannel messageChannel;

    @RequestMapping(method = RequestMethod.POST)
    public void write (@RequestBody Reservation reservation){
        this.messageChannel.send(MessageBuilder.withPayload(reservation.getReservationName()).build());
    }

    @HystrixCommand(fallbackMethod = "getReservationNamesFallback")
    @RequestMapping("/names")
    public Collection<String> getReservationNames(){

        //Helps json to parametrized conversion of json into given types
        ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {};

        ResponseEntity<Resources<Reservation>> responseEntity =
                this.restTemplate.exchange(
                        "http://reservation-service/reservations",
                        HttpMethod.GET,
                        null,
                        ptr
                );

        return responseEntity
                .getBody()
                .getContent()
                .stream()
                .map(Reservation::getReservationName)
                .collect(Collectors.toList());
    }

    Collection<String> getReservationNamesFallback(){
        return Collections.EMPTY_LIST;
    }

}
