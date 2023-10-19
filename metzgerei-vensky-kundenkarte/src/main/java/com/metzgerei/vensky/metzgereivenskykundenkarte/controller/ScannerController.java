package com.metzgerei.vensky.metzgereivenskykundenkarte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ScannerController {

    @Autowired
    private ScannerService scannerService;

    @PostMapping("/scan")
    public ResponseEntity<String> scanQRCode(@RequestBody String scannedQRCodeContent) {
        // Implementiere hier die Logik für das Scannen des QR-Codes und die Datenbanküberprüfung.
        boolean isValidQRCode = scannerService.validateQRCode(scannedQRCodeContent);

        if (isValidQRCode) {
            // Der QR-Code ist gültig, du kannst hier weitere Aktionen ausführen, z.B. Punkte hinzufügen.
            return ResponseEntity.ok("QR-Code gültig und aktiviert");
        } else {
            // Der QR-Code ist ungültig.
            return ResponseEntity.badRequest().body("Ungültiger QR-Code.");
        }
    }

    @GetMapping("/points/{cardId}")
    public String getPointsPage(@PathVariable String cardId, Model model) {
        CardModel cardModel = scannerService.getCardByCardId(cardId);

        if (cardModel != null && "aktiv".equals(cardModel.getStatus())) {
            model.addAttribute("card", cardModel);
            return "points"; // Gibt die points.html-Vorlage zurück
        } else {
            // Wenn die Karte nicht gefunden oder inaktiv ist, leite zu einer Fehlerseite oder zeige eine Fehlermeldung an.
            return "errorpage"; // Beispiel: Fehlerseite mit einer Nachricht "Ungültige Karte"
        }
    }
}
