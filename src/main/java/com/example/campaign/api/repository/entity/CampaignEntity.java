package com.example.campaign.api.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CampaignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String campaignName;
    private String campaignDescription;
    private String campaignCategory;
    private String campaignStatus;

    //TODO bu alanı doldur
    private LocalDateTime createdDatetime;

}
