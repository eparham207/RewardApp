package com.itccloud.RewardProject.repository;

import com.itccloud.RewardProject.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    // Find all seats for a specific stand
    List<Seat> findByStandName(String standName);
}