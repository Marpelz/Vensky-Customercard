package com.metzgerei.vensky.metzgereivenskykundenkarte;

import com.metzgerei.vensky.metzgereivenskykundenkarte.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.metzgerei.vensky.metzgereivenskykundenkarte.models")
public class CardApp {

	@Autowired
	private CardService cardService;

	public static void main(String[] args) {
		SpringApplication.run(CardApp.class, args);
	}

	/*@Bean
	public CommandLineRunner generateQRCodes() {
		return args -> {
			// Hier kannst du die Methode aus dem CardService aufrufen, um die QR-Codes zu generieren und zu speichern.
			int createCodes = 8; // Generiert Anzahl an angegebenen Codes
			cardService.generateAndSaveCard(createCodes);
		};
	}*/

	/*@Bean
	public CommandLineRunner generateQRCodePNG() {
		return args -> {
			// Rufe die Methode aus dem CardService auf, um Abbildungen für die hinterlegten QR-Codes zu generieren.
			cardService.generateQRCodeImages();
		};
	}*/
}
