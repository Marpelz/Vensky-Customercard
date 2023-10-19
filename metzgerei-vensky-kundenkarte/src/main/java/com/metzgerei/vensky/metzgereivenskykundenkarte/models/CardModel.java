package com.metzgerei.vensky.metzgereivenskykundenkarte.models;

import jakarta.persistence.*;

@Entity
@Table(name = "customercards")
public class CardModel {

    @Id
    private Long cardId;
    @Column(name = "qrCode")
    private String qrCode;
    @Column(name = "points")
    private int points;
    @Column(name = "status")
    private String status;

    public CardModel() {
    }

    public CardModel(Long cardId, String qrCode, int points, String status) {
        this.cardId = cardId;
        this.qrCode = qrCode;
        this.points = points;
        this.status = status;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
