package com.itccloud.RewardProject.controller;

import com.itccloud.RewardProject.model.Reward;
import com.itccloud.RewardProject.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class RewardController {

    @Autowired
    private RewardRepository rewardRepository;

    @GetMapping("/reward")
    public String showRewardPage(Model model) {
        // Get results, sorted by preferred stand as required [cite: 45]
        List<Reward> rewards = rewardRepository.findAllByOrderByFanPreferredStand();
        model.addAttribute("rewards", rewards);
        return "reward-page"; // This is our new HTML file
    }
}
