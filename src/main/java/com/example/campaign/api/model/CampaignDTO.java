package com.example.campaign.api.model;

import lombok.Data;

@Data
public class CampaignDTO {
    private Long id;
    private String campaignName;
    private String campaignDescription;
    private String campaignCategory;
    private String campaignStatus;
}
