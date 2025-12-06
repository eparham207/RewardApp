package com.itccloud.RewardProject.controller;

import com.itccloud.RewardProject.model.FanPreference;
import com.itccloud.RewardProject.model.StandSummary;
import com.itccloud.RewardProject.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PreferenceController {
	
	// This method handles the "Reward" button click
    @PostMapping("/preference/reward")
    public String processRewards(RedirectAttributes redirectAttributes) {
        try {
            preferenceService.processRewards();
            // On success, go to the new reward page [cite: 29]
            return "redirect:/reward"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing rewards: " + e.getMessage());
            return "redirect:/preference";
        }
    }
	// This method handles the "Import and Append" button file upload
    @PostMapping("/preference/import-append")
    public String importAndAppendPreferences(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            preferenceService.importAndAppendPreferences(file);
            redirectAttributes.addFlashAttribute("successMessage", "File imported successfully and appended to existing records.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/preference";
    }

    @Autowired
    private PreferenceService preferenceService;

    // This method shows the page
    @GetMapping("/preference")
    public String showPreferencePage(Model model) {
        List<StandSummary> summaries = preferenceService.getPreferenceSummary();
        model.addAttribute("summaries", summaries);
        // Add an empty FanPreference object to bind to the "Add" form
        model.addAttribute("newFan", new FanPreference()); 
        return "preference-page";
    }

    // This method handles the "Add" button form submission
    @PostMapping("/preference/add")
    public String addPreference(@ModelAttribute FanPreference newFan, RedirectAttributes redirectAttributes) {
        try {
            preferenceService.addPreference(newFan);
            redirectAttributes.addFlashAttribute("successMessage", "Fan preference added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/preference";
    }

    // This method handles the "Import" button file upload
    @PostMapping("/preference/import")
    public String importPreferences(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            preferenceService.importPreferences(file);
            redirectAttributes.addFlashAttribute("successMessage", "File imported successfully! All old records have been replaced.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/preference";
    }
}