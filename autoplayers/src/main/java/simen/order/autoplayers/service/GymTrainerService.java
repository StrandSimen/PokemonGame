package simen.order.autoplayers.service;

import org.springframework.stereotype.Service;
import simen.order.autoplayers.model.GymTrainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GymTrainerService {

    private final Map<String, GymTrainer> trainers = new HashMap<>();

    public GymTrainerService() {
        initializeTrainers();
    }

    private void initializeTrainers() {
        // Fire Trainer - Blaine with Fire types
        trainers.put("Blaine", new GymTrainer(
                "Blaine",
                "Fire",
                Arrays.asList(38, 59, 6) // Ninetales, Arcanine, Charizard
        ));

        // Water Trainer - Misty with Water types
        trainers.put("Misty", new GymTrainer(
                "Misty",
                "Water",
                Arrays.asList(121, 130, 9) // Starmie, Gyarados, Blastoise
        ));

        // Grass Trainer - Erika with Grass types
        trainers.put("Erika", new GymTrainer(
                "Erika",
                "Grass",
                Arrays.asList(45, 71, 3) // Vileplume, Victreebel, Venusaur
        ));
    }

    public List<String> getAvailableTrainers() {
        return List.copyOf(trainers.keySet());
    }

    public GymTrainer getTrainer(String name) {
        GymTrainer trainer = trainers.get(name);
        if (trainer == null) {
            throw new RuntimeException("Trainer not found: " + name + ". Available trainers: " + String.join(", ", trainers.keySet()));
        }
        return trainer;
    }

    public Map<String, GymTrainer> getAllTrainers() {
        return new HashMap<>(trainers);
    }
}

