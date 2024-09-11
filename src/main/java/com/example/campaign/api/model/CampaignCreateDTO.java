package com.example.campaign.api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CampaignCreateDTO {

    @NotEmpty(message = "Campaign Name is required")
    @Size(min = 10, max = 50, message = "Campaign Name must be between 10 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9çÇğĞıİöÖşŞüÜ ]*$", message = "Campaign Name must not contain special characters")
    private String campaignName;

    @NotEmpty(message = "Campaign Description is required")
    @Size(min = 20, max = 200, message = "Campaign Description must be between 20 and 200 characters")
    private String campaignDescription;

    @NotNull(message = "Campaign category is required")
    @Pattern(regexp = "TSS|OSS|HAYAT|OTHER", message = "Invalid category. Allowed values are: TSS, OSS, HAYAT, OTHER")
    private String campaignCategory;
}
