package com.itccloud.RewardProject.config;

import com.itccloud.RewardProject.model.Seat;
import com.itccloud.RewardProject.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only run this if the seats table is empty
        if (seatRepository.count() == 0) {
            // East stand seats 
            seatRepository.saveAll(Arrays.asList(
                new Seat("E001", "East Stand"), new Seat("E002", "East Stand"), new Seat("E003", "East Stand")
            ));
            // West stand seats 
            seatRepository.saveAll(Arrays.asList(
                new Seat("W501", "West Stand"), new Seat("W502", "West Stand"), new Seat("W503", "West Stand")
            ));
            // South stand seats 
            seatRepository.saveAll(Arrays.asList(
                new Seat("S101", "South Stand"), new Seat("S102", "South Stand"), new Seat("S103", "South Stand"),
                new Seat("S104", "South Stand"), new Seat("S105", "South Stand")
            ));
            // North stand seats 
            seatRepository.saveAll(Arrays.asList(
                new Seat("N601", "North Stand"), new Seat("N602", "North Stand"), new Seat("N603", "North Stand"),
                new Seat("N604", "North Stand"), new Seat("N605", "North Stand")
            ));
        }
    }
}
