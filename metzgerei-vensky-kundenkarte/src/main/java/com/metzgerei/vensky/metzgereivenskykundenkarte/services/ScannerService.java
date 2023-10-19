package com.metzgerei.vensky.metzgereivenskykundenkarte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScannerService {
    
    @Autowired
    private CardRepository cardRepository;

    public boolean validateQRCode(String scannedQRCode) {

        String cardId = extractCardId(scannedQRCode);

        if (cardId != null) {
            Optional<CardModel> existingCard = cardRepository.findByCardId(cardId);

            if (existingCard.isPresent()) {
                CardModel cardModel = existingCard.get();
                if ("ja".equals(cardModel.getStatus())) {
                    // Überprüfe, ob der Status "ja" (aktiv) ist, bevor du weitere Aktionen ausführst
                    cardModel.setPoints(cardModel.getPoints() + 15); // Beispiel: Den Punktestand um 15 erhöhen
                    cardRepository.save(cardModel);
                    return true; // QR-Code ist gültig und aktiviert
                }
            }
        }
        return false; // QR-Code ist ungültig oder inaktiv
    }

    // Annahme: Implementieren Sie die Methode zur Extraktion der Karten-ID aus dem QR-Code
    private String extractCardId(String scannedQRCode) {

        // Geben Sie die Karten-ID zurück oder null, wenn sie nicht gefunden wird.
        // Dies kann je nach QR-Code-Format und Anforderungen variieren.
        try {
            // Suchen Sie nach einem 16-stelligen Muster in dem gescannten QR-Code.
            Pattern pattern = Pattern.compile("[A-Za-z0-9]");
            Matcher matcher = pattern.matcher(scannedQRCode);

            if (matcher.find()) {
                String cardId = matcher.group();

                if (cardId.length() == 16) {
                    // Die extrahierte Zeichenfolge hat die richtige Länge (16 Zeichen).
                    return cardId;
                }
            }
        } catch (Exception e) {
            // Möglicherweise trat ein Fehler auf, wenn der QR-Code nicht das erwartete Format hatte.
            // Hier können Sie Fehlerprotokollierung oder -behandlung hinzufügen.
        }

        // Rückgabe von null, wenn die Karten-ID nicht gefunden, nicht korrekt formatiert oder ein Fehler aufgetreten ist.
        return null;
    }

    public CardModel getCardByCardId(String cardId) {
        return cardRepository.findByCardId(cardId)
                .orElse(null);
    }
}
