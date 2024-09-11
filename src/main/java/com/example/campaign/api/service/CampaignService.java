package com.example.campaign.api.service;

import com.example.campaign.api.model.CampaignDTO;
import com.example.campaign.api.model.CampaignCreateDTO;
import com.example.campaign.api.repository.CampaignRepository;
import com.example.campaign.api.repository.entity.CampaignEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    // Kampanya oluşturma metodu
    public CampaignDTO createCampaign(CampaignCreateDTO campaignCreateDTO) {
        // DTO'dan Entity'ye dönüşüm
        CampaignEntity campaignEntity = new CampaignEntity();
        campaignEntity.setCampaignName(campaignCreateDTO.getCampaignName());
        campaignEntity.setCampaignDescription(campaignCreateDTO.getCampaignDescription());
        campaignEntity.setCampaignCategory(campaignCreateDTO.getCampaignCategory());
        campaignEntity.setCampaignStatus(decideCampaignStatus(campaignEntity));
        campaignEntity.setCreatedDatetime(LocalDateTime.now()); // createdDatetime'i ayarla

        // Veritabanına kaydetme
        campaignRepository.save(campaignEntity);

        // DTO oluşturma
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setId(campaignEntity.getId());
        campaignDTO.setCampaignName(campaignEntity.getCampaignName());
        campaignDTO.setCampaignDescription(campaignEntity.getCampaignDescription());
        campaignDTO.setCampaignCategory(campaignEntity.getCampaignCategory());
        campaignDTO.setCampaignStatus(campaignEntity.getCampaignStatus());

        return campaignDTO;
    }

    // Kampanya statüsüne karar verme
    private String decideCampaignStatus(CampaignEntity campaignEntity) {
        // Mükerrer kontrolü
        boolean isDuplicate = campaignRepository.existsByCampaignNameAndCampaignDescription(
                campaignEntity.getCampaignName(),
                campaignEntity.getCampaignDescription());

        if (isDuplicate) {
            return "MUKERRER"; // Mükerrer kampanya
        } else {
            // Kategoriye göre statü belirleme
            String category = campaignEntity.getCampaignCategory();
            if ("HAYAT".equals(category)) {
                return "AKTIF";
            } else {
                return "ONAY BEKLIYOR"; // Varsayılan olarak "Onay Bekliyor"
            }
        }
    }

    // Kampanyaları statüye göre filtreleme
    public List<CampaignDTO> getCampaignsByStatus(String status) {
        List<CampaignEntity> campaigns;

        if (status == null || status.isEmpty()) {
            campaigns = campaignRepository.findAll(); // Tüm kampanyaları getir
        } else {
            campaigns = campaignRepository.findByCampaignStatusIn(Arrays.asList(status.split(",")));
        }

        // Kampanyaları DTO'ya dönüştür
        return campaigns.stream()
                .map(campaign -> {
                    CampaignDTO dto = new CampaignDTO();
                    dto.setId(campaign.getId());
                    dto.setCampaignName(campaign.getCampaignName());
                    dto.setCampaignDescription(campaign.getCampaignDescription());
                    dto.setCampaignCategory(campaign.getCampaignCategory());
                    dto.setCampaignStatus(campaign.getCampaignStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Kampanya statüsünü güncelleme
    public void updateCampaignStatus(Long campaignId, String status) {
        CampaignEntity campaignEntity = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Kampanya bulunamadı: " + campaignId));

        // Statü kuralı kontrolü
        if (isValidStatus(status)) {
            campaignEntity.setCampaignStatus(status);
            campaignRepository.save(campaignEntity);
        } else {
            throw new IllegalArgumentException("Geçersiz statü: " + status);
        }
    }

    private boolean isValidStatus(String status) {
        // Geçerli statüler
        return Arrays.asList("AKTIF", "ONAY BEKLIYOR", "MUKERRER").contains(status);
    }

    // Kampanyaları kategoriye göre filtreleme
    public List<CampaignDTO> getCampaignsByCategory(String category) {
        List<CampaignEntity> campaigns = campaignRepository.findByCampaignCategory(category);

        // Kampanya Entity'lerini DTO'ya çevirme
        return campaigns.stream()
                .map(campaign -> {
                    CampaignDTO dto = new CampaignDTO();
                    dto.setId(campaign.getId());
                    dto.setCampaignName(campaign.getCampaignName());
                    dto.setCampaignDescription(campaign.getCampaignDescription());
                    dto.setCampaignCategory(campaign.getCampaignCategory());
                    dto.setCampaignStatus(campaign.getCampaignStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
