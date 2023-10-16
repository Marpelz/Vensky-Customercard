package com.metzgerei.vensky.metzgereivenskykundenkarte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardModel, String> {

    Optional<CardModel> findByCardId(String cardId);
}
