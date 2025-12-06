package com.itccloud.RewardProject.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itccloud.RewardProject.model.FanPreference;
import com.itccloud.RewardProject.model.Reward;
import com.itccloud.RewardProject.model.Seat;
import com.itccloud.RewardProject.model.StandSummary;
import com.itccloud.RewardProject.repository.FanPreferenceRepository;
import com.itccloud.RewardProject.repository.RewardRepository;
import com.itccloud.RewardProject.repository.SeatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreferenceService {
	
	// Add these new autowired fields at the top of the class
	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private RewardRepository rewardRepository;

	// --- ADD THIS MAIN METHOD ---
	@Transactional
	public void processRewards() {
	    // Clicking Reward restarts the process, so clear old rewards [cite: 43]
	    rewardRepository.deleteAll();

	    List<Reward> allRewards = new ArrayList<>();

	    // Process each stand based on its rules
	    allRewards.addAll(processFCFSStand("East Stand", 3)); // [cite: 34-36]
	    allRewards.addAll(processFCFSStand("West Stand", 3)); // [cite: 37]
	    allRewards.addAll(processRandomStand("South Stand", 5)); // [cite: 38-39]
	    allRewards.addAll(processWeightedRandomNorthStand("North Stand", 5)); // [cite: 40-41]

	    // Save all the new rewards to the database [cite: 42]
	    rewardRepository.saveAll(allRewards);
	}

	// --- ADD THIS HELPER METHOD for East/West ---
	private List<Reward> processFCFSStand(String standName, int limit) {
	    List<Reward> rewards = new ArrayList<>();
	    // Get fans ordered by reservation time [cite: 35]
	    List<FanPreference> fans = fanRepository.findByPreferredStandOrderByReservationTimeAsc(standName);
	    List<Seat> seats = seatRepository.findByStandName(standName);

	    int winnersCount = Math.min(Math.min(fans.size(), seats.size()), limit);

	    for (int i = 0; i < winnersCount; i++) {
	        Reward reward = new Reward();
	        reward.setFan(fans.get(i));
	        reward.setSeat(seats.get(i));
	        rewards.add(reward);
	    }
	    return rewards;
	}

	// --- ADD THIS HELPER METHOD for South ---
	private List<Reward> processRandomStand(String standName, int limit) {
	    List<Reward> rewards = new ArrayList<>();
	    List<FanPreference> fans = fanRepository.findByPreferredStand(standName);
	    List<Seat> seats = seatRepository.findByStandName(standName);

	    // Randomize the fans list [cite: 38]
	    Collections.shuffle(fans);

	    int winnersCount = Math.min(Math.min(fans.size(), seats.size()), limit);

	    for (int i = 0; i < winnersCount; i++) {
	        Reward reward = new Reward();
	        reward.setFan(fans.get(i)); // Get from the shuffled list
	        reward.setSeat(seats.get(i));
	        rewards.add(reward);
	    }
	    return rewards;
	}

	// --- ADD THIS HELPER METHOD for North ---
	private List<Reward> processWeightedRandomNorthStand(String standName, int limit) {
	    List<Reward> rewards = new ArrayList<>();
	    List<FanPreference> fans = fanRepository.findByPreferredStand(standName);
	    List<Seat> seats = seatRepository.findByStandName(standName);

	    if (fans.isEmpty() || seats.isEmpty()) {
	        return rewards; // No fans or no seats, nothing to do
	    }

	    // 1. Create a weighted list of fans
	    List<FanPreference> weightedFanList = new ArrayList<>();
	    for (FanPreference fan : fans) {
	        // Military fans (MLT) get a 4x higher chance (0.8 vs 0.2) 
	        int weight = 1; // Default weight for OTH, STU, EDU
	        if ("MLT".equals(fan.getOccupation())) {
	            weight = 4; // 4:1 ratio simulates 0.8 vs 0.2
	        }
	        for (int i = 0; i < weight; i++) {
	            weightedFanList.add(fan);
	        }
	    }

	    // 2. Shuffle the big weighted list
	    Collections.shuffle(weightedFanList);

	    // 3. Pick unique winners from the shuffled list
	    List<FanPreference> winners = new ArrayList<>();
	    int seatsAvailable = Math.min(seats.size(), limit);

	    for (FanPreference weightedFan : weightedFanList) {
	        if (winners.size() >= seatsAvailable) {
	            break; // We have all the winners we can assign
	        }
	        // Only add if they haven't been picked already
	        if (!winners.contains(weightedFan)) {
	            winners.add(weightedFan);
	        }
	    }

	    // 4. Assign seats to the unique winners
	    for (int i = 0; i < winners.size(); i++) {
	        Reward reward = new Reward();
	        reward.setFan(winners.get(i));
	        reward.setSeat(seats.get(i));
	        rewards.add(reward);
	    }
	    return rewards;
	}
	// Logic for the "Import and Append" button
    @Transactional
    public void importAndAppendPreferences(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Cannot import an empty file.");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); 

        try {
            List<FanPreference> fansToImport = mapper.readValue(file.getInputStream(), new TypeReference<>() {});

            // **CRITICAL STEP**: Check for duplicates before appending
            for (FanPreference fan : fansToImport) {
                if (fanRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(fan.getFirstName(), fan.getLastName())) {
                    throw new Exception("Duplicate fan found in file: '" + fan.getFirstName() + " " + fan.getLastName() + "'. Import aborted.");
                }
            }
            
            // **DIFFERENCE**: We DO NOT call fanRepository.deleteAll() here
            
            // Save all the new records from the file
            fanRepository.saveAll(fansToImport);

        } catch (IOException e) {
            throw new Exception("Failed to parse JSON file: " + e.getMessage());
        }
    }

    @Autowired
    private FanPreferenceRepository fanRepository;

    // This method now calculates the summary from the database
    public List<StandSummary> getPreferenceSummary() {
        // We still define the basic stand info here or from a config file
        List<String> standNames = Arrays.asList("North Stand", "South Stand", "East Stand", "West Stand");

        return standNames.stream().map(name -> {
            StandSummary summary = new StandSummary();
            summary.setStandName(name);
            // Count preferences for each stand from the database
            summary.setPreferredSeats((int) fanRepository.countByPreferredStand(name));
            
            // These values can still be hardcoded or moved to application.properties
            // For simplicity, we'll set them here.
            if (name.equals("North Stand")) {
                summary.setAvailableSeats(150);
                summary.setDiscountPrice(10.0);
            } else if (name.equals("South Stand")) {
                summary.setAvailableSeats(150);
                summary.setDiscountPrice(15.0);
            } else if (name.equals("East Stand")) {
                summary.setAvailableSeats(150);
                summary.setDiscountPrice(12.0);
            } else if (name.equals("West Stand")) {
                summary.setAvailableSeats(150);
                summary.setDiscountPrice(17.0);
            }
            return summary;
        }).collect(Collectors.toList());
    }
    
    // Logic for the "Add" button
    public void addPreference(FanPreference fan) throws Exception {
        // Validation: Check for duplicate names (case-insensitive)
        if (fanRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(fan.getFirstName(), fan.getLastName())) {
            throw new Exception("A fan with the name '" + fan.getFirstName() + " " + fan.getLastName() + "' already exists.");
        }
        // Set the reservation time to now
        fan.setReservationTime(LocalDateTime.now());
        fanRepository.save(fan);
    }
    
    // Logic for the "Import" button
    @Transactional // This ensures the whole operation succeeds or fails together
    public void importPreferences(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Cannot import an empty file.");
        }

        ObjectMapper mapper = new ObjectMapper();
        // Tell Jackson how to handle date/time formats if they are in your JSON
        mapper.findAndRegisterModules(); 
        
        try {
            // Read the JSON file into a list of FanPreference objects
            List<FanPreference> fansToImport = mapper.readValue(file.getInputStream(), new TypeReference<>() {});

            // Important: The "Import" button deletes all existing records first
            fanRepository.deleteAll();
            
            // Save all the new records from the file
            fanRepository.saveAll(fansToImport);

        } catch (IOException e) {
            throw new Exception("Failed to parse JSON file: " + e.getMessage());
        }
    }
}