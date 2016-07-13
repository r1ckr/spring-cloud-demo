package com.example;

import com.example.model.Reservation;
import com.example.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;


import java.util.Arrays;

@EnableBinding(Sink.class)
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {


	@Bean
	AlwaysSampler alwaysSampler(){
		return new AlwaysSampler();
	}

	@Bean
	CommandLineRunner runner (ReservationRepository reservationRepository){

		return args -> {
			//Here we are splitting names into a list, then for each element we are saving that as a new reservation
			Arrays.asList("John,Doe,Mario,Richard".split(","))
					.forEach( x -> reservationRepository.save(new Reservation(x)));
			//Here we are searching every element of the list and printing them
			reservationRepository.findAll().forEach(System.out::println);
		};

	}

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
}
