package com.example;


import com.example.model.Reservation;
import com.example.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class MessageReservationReceiver {

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void acceptReservation(String rn){
        this.reservationRepository.save(new Reservation(rn));
    }

    @Autowired
    private ReservationRepository reservationRepository;
}
