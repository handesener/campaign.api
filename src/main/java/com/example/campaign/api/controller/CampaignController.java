package com.example.campaign.api.controller;

import com.example.campaign.api.model.CampaignCreateDTO;
import com.example.campaign.api.model.CampaignDTO;
import com.example.campaign.api.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping
    public ResponseEntity<?> campaignCreate(@Valid @RequestBody CampaignCreateDTO campaignCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        CampaignDTO campaignDTO = campaignService.createCampaign(campaignCreateDTO);
        return new ResponseEntity<>(campaignDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getCampaignsByStatus(@RequestParam(required = false) String status) {
        List<CampaignDTO> campaigns = campaignService.getCampaignsByStatus(status);
        return new ResponseEntity<>(campaigns, HttpStatus.OK);
    }

    // Yeni eklediğin kategoriye göre listeleme endpoint'i
    @GetMapping("/byCategory")
    public ResponseEntity<?> getCampaignsByCategory(@RequestParam String category) {
        List<CampaignDTO> campaigns = campaignService.getCampaignsByCategory(category);
        return new ResponseEntity<>(campaigns, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updateCampaignStatus(@RequestParam Long campaignId, @RequestParam String status) {
        campaignService.updateCampaignStatus(campaignId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
