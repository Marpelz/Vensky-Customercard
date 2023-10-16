package com.metzgerei.vensky.metzgereivenskykundenkarte;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService {

    // Abhängigkeiten
    @Autowired(required = true)
    private CardRepository cardRepository;

    public void generateAndSaveCard(int createCodes) {
        for (int i = 0; i < createCodes; i++) {
            String qrCodeContent;
            boolean isUnique;

            do {
                // Generiere eine zufällige 16-stellige Kombination
                qrCodeContent = generateRandomQRCodeContent();

                // Überprüfe, ob die Kombination bereits in der Datenbank existiert
                isUnique = isQRCodeContentUnique(qrCodeContent);
            } while (!isUnique);

            // Speichere die Karte mit dem generierten QR-Code und Status "nein" (inaktiv)
            CardModel cardModel = new CardModel();
            cardModel.setCardId(qrCodeContent);
            cardModel.setPoints(0);
            cardModel.setStatus("nein");
            cardRepository.save(cardModel);

            // Erstelle den QR-Code als Bild und speichere ihn
            generateQRCodeImage(qrCodeContent);
        }
    }

    private boolean isQRCodeContentUnique(String qrCodeContent) {
        Optional<CardModel> existingCard = cardRepository.findByCardId(qrCodeContent);
        return existingCard.isEmpty();
    }

    private String generateRandomQRCodeContent() {
        // Hier generierst du eine zufällige 16-stellige Zeichenkette
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder qrCodeContent = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            qrCodeContent.append(characters.charAt(randomIndex));
        }
        return qrCodeContent.toString();
    }

    private void generateQRCodeImage(String qrCodeContent) {
        // Erstelle den QR-Code als Bild und speichere ihn
        QRCode.from(qrCodeContent)
                .withSize(250, 250) // Größe des QR-Codes
                .to(ImageType.PNG)
                .file("src/main/resources/qrcodes/" + qrCodeContent + ".png");
    }
}
