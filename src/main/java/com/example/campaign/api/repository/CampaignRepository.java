package com.example.campaign.api.repository;

import com.example.campaign.api.repository.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, Long> {
    boolean existsByCampaignNameAndCampaignDescription(String campaignName, String campaignDescription);

    // Kampanyaları statüsüne göre getiren sorgu
    List<CampaignEntity> findByCampaignStatusIn(List<String> statuses);

    // Kategoriye göre kampanyaları getiren sorgu
    List<CampaignEntity> findByCampaignCategory(String category);
}
