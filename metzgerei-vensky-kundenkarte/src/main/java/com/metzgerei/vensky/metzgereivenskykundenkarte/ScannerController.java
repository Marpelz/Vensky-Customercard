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
    public ResponseEntity<String> scanQRCode(@RequestBody String scannedQRCode) {
        // Implementieren Sie hier die Logik für das Scannen des QR-Codes und die Datenbankverarbeitung.
        scannerService.processQRCode(scannedQRCode);
        return ResponseEntity.ok("QR-Code gescannt und verarbeitet.");
    }

    @GetMapping("/points/{cardId}")
    public String getPointsPage(@PathVariable String cardId, Model model) {
        Card card = scannerService.getCardByCardId(cardId);
        model.addAttribute("card", card);
        return "points"; // Gibt die points.html-Vorlage zurück
    }
}
