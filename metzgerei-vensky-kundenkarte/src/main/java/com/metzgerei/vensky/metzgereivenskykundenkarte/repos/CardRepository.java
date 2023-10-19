package com.metzgerei.vensky.metzgereivenskykundenkarte.repos;

import com.metzgerei.vensky.metzgereivenskykundenkarte.models.CardModel;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CardModel> findAll() {
        return entityManager.createQuery("SELECT c FROM CardModel c", CardModel.class).getResultList();
    }

    public CardModel findByQrCode(String qrCode) {
        try {
            return entityManager.createQuery("SELECT c FROM CardModel c WHERE c.qrCode = :qrCode", CardModel.class)
                    .setParameter("qrCode", qrCode)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public CardModel findByCardId(long cardId) {
        try {
            return entityManager.createQuery("SELECT c FROM CardModel c WHERE c.cardId = :cardId", CardModel.class)
                    .setParameter("cardId", cardId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public CardModel save(CardModel cardModel){
        entityManager.persist(cardModel);
        return cardModel;
    }

    @Transactional
    public CardModel update(CardModel cardModel){
        entityManager.merge(cardModel);
        return cardModel;
    }
}
