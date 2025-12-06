package com.itccloud.RewardProject.repository;

import com.itccloud.RewardProject.model.FanPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FanPreferenceRepository extends JpaRepository<FanPreference, Long> {

    // Custom method to check if a fan with the same first and last name exists (case-insensitive)
    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    
    // Custom method to count preferences for a specific stand
    long countByPreferredStand(String standName);
    
 // Find fans for a stand, ordered by time for FCFS [cite: 34]
    List<FanPreference> findByPreferredStandOrderByReservationTimeAsc(String standName);

    // Find all fans for a stand (for random selection)
    List<FanPreference> findByPreferredStand(String standName);
}