package simen.order.autoplayers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simen.order.autoplayers.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
}

