package simen.order.autoplayers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simen.order.autoplayers.model.GymBattle;

import java.util.List;

@Repository
public interface GymBattleRepository extends JpaRepository<GymBattle, Long> {
    List<GymBattle> findByUsername(String username);
    List<GymBattle> findByUsernameAndPlayerWon(String username, boolean playerWon);
}

