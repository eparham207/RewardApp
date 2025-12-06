package com.itccloud.RewardProject.repository;

import com.itccloud.RewardProject.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    // Get all rewards, sorted by stand for the reward page [cite: 45]
    List<Reward> findAllByOrderByFanPreferredStand();
}
