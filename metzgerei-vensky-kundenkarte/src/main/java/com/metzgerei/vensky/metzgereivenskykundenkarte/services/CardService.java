package com.metzgerei.vensky.metzgereivenskykundenkarte.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.metzgerei.vensky.metzgereivenskykundenkarte.models.CardModel;
import com.metzgerei.vensky.metzgereivenskykundenkarte.repos.CardRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

@Service
public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private EntityManager entityManager;
    private static final String QR_CODE_DIRECTORY = "src/main/resources/qrcodes/";

    @Transactional
    public void generateAndSaveCard(int createCodes) throws SQLException {
        for (int i = 0; i < createCodes; i++) {
            long cardId;
            String qrCodeContent;

            do {
                cardId = generateRandomCardId();
                qrCodeContent = generateRandomQRCodeContent();
            } while (!isQRCodeContentUnique(qrCodeContent) && !isCardIdContentUnique(cardId));

            CardModel cardModel = new CardModel(cardId, qrCodeContent, 0, "nicht aktiv");

            // Save the entity
            entityManager.persist(cardModel);

            entityManager.flush();

            generateQRCodeImage(qrCodeContent);
        }
    }

    private long generateRandomCardId() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }

    private boolean isCardIdContentUnique(long cardId) {
        return cardRepository.findByCardId(cardId) == null;
    }

    private String generateRandomQRCodeContent() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder qrCodeContent = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            qrCodeContent.append(characters.charAt(randomIndex));
        }
        return qrCodeContent.toString();
    }

    private boolean isQRCodeContentUnique(String qrCodeContent) {
        // Überprüfe, ob ein CardModel mit dem QR-Code bereits in der Datenbank existiert
        return cardRepository.findByQrCode(qrCodeContent) == null;
    }

    // QR-Code-Generator (Generiert Abildungen der hinterlegten QR-Codes in der Datenbank)
    @Transactional
    public void generateQRCodeImages() {
        List<CardModel> cards = cardRepository.findAll();

        for (CardModel card : cards) {
            String qrCodeContent = card.getQrCode();
            long cardId = card.getCardId();
            String qrCodeFileName = cardId + ".png";

            // Generiere die QR-Code-Abbildung
            File qrCodeFile = generateQRCodeImage(qrCodeContent);

            // Speichere die Abbildung im Verzeichnis
            saveQRCodeImage(qrCodeFile, qrCodeFileName);
        }
    }

    private File generateQRCodeImage(String qrCodeContent) {
        File qrCodeFile = new File(QR_CODE_DIRECTORY + qrCodeContent + ".png");
        int width = 200;
        int height = 200;

        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, width, height, hints);

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", new FileOutputStream(qrCodeFile));

        } catch (Exception e) {
            logger.error("Fehler beim Generieren des QR-Codes", e);
        }

        return qrCodeFile;
    }

    private void saveQRCodeImage(File qrCodeFile, String fileName) {
        // Stellen Sie sicher, dass das Verzeichnis existiert
        boolean dirCreated = qrCodeFile.getParentFile().mkdirs();
        if (!dirCreated) {
            logger.warn("Das Verzeichnis für QR-Codes wurde nicht erstellt.");
        }

        // Kopieren Sie die Datei in den gewünschten Dateinamen
        File newFile = new File(QR_CODE_DIRECTORY + fileName);

        try {
            FileCopyUtils.copy(qrCodeFile, newFile);
            logger.info("QR-Code-Abbildung gespeichert als: {}", newFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Fehler beim Speichern der QR-Code-Abbildung.", e);
        }
    }
}
