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

    public void processQRCode(String scannedQRCode) {
        // Implementieren Sie hier die Logik für das Scannen des QR-Codes und die Datenbankverarbeitung.
        // Annahme: Der gescannte QR-Code enthält die Karten-ID
        String cardId = extractCardId(scannedQRCode);

        if (cardId != null) {
            // Überprüfen, ob die Karten-ID bereits in der Datenbank existiert
            Optional<Card> existingCard = cardRepository.findByCardId(cardId);

            if (existingCard.isPresent()) {
                // Die Karten-ID existiert bereits in der Datenbank, aktualisieren Sie den Punktestand oder führen Sie andere Aktionen aus.
                Card card = existingCard.get();
                card.setPoints(card.getPoints() + 15); // Beispiel: Den Punktestand um 1 erhöhen
                cardRepository.save(card); // Speichern Sie die aktualisierte Karte in der Datenbank
            } else {
                // Die Karten-ID existiert nicht in der Datenbank, erstellen Sie einen neuen Datensatz.
                Card newCard = new Card();
                newCard.setCardId(cardId);
                newCard.setPoints(10); // Beispiel: Punktestand auf 1 setzen
                cardRepository.save(newCard); // Speichern Sie die neue Karte in der Datenbank
            }
        }
    }

    // Annahme: Implementieren Sie die Methode zur Extraktion der Karten-ID aus dem QR-Code
    private String extractCardId(String scannedQRCode) {
        // Implementieren Sie die Logik zur Extraktion der Karten-ID aus dem gescannten QR-Code
        // Geben Sie die Karten-ID zurück oder null, wenn sie nicht gefunden wird.
        // Dies kann je nach QR-Code-Format und Anforderungen variieren.
        try {
            // Suchen Sie nach einem 16-stelligen Muster in dem gescannten QR-Code.
            Pattern pattern = Pattern.compile("[A-Za-z0-9!@#$%^&*()-_=+<>?]+");
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

    public Card getCardByCardId(String cardId) {
        return cardRepository.findByCardId(cardId)
                .orElse(null);
    }
}
