package simen.order.boosterpack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simen.order.boosterpack.model.Card;

import java.util.List;

@Repository
public interface CardRepo extends JpaRepository<Card, Integer> {
    List<Card> findAllByOrderByPokedexNumberAsc();
}