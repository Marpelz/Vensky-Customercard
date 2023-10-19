package com.metzgerei.vensky.metzgereivenskykundenkarte.services;

import com.metzgerei.vensky.metzgereivenskykundenkarte.models.CardModel;
import com.metzgerei.vensky.metzgereivenskykundenkarte.repos.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScannerService {

    @Autowired
    private CardRepository cardRepository;

    public boolean validateQRCode(String scannedQRCode) {
        CardModel existingCard = cardRepository.findByQrCode(scannedQRCode);

        if (existingCard != null) {
            if ("nicht aktiv".equals(existingCard.getStatus())) {
                existingCard.setStatus("aktiv");
                existingCard.setPoints(existingCard.getPoints() + 25);
                cardRepository.update(existingCard);
                return true;
            }
            else if ("aktiv".equals((existingCard.getStatus()))) {
                existingCard.setPoints(existingCard.getPoints() + 15);
                cardRepository.update(existingCard);
                return true;
            }
        }
        return false;
    }

    public CardModel getCardByQrCode(String qrCode) {
        return cardRepository.findByQrCode(qrCode);
    }
}
