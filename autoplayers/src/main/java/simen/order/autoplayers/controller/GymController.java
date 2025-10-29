package simen.order.autoplayers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simen.order.autoplayers.dto.BattleRequest;
import simen.order.autoplayers.dto.BattleResult;
import simen.order.autoplayers.model.GymBattle;
import simen.order.autoplayers.model.GymTrainer;
import simen.order.autoplayers.service.GymBattleService;
import simen.order.autoplayers.service.GymTrainerService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gym")
public class GymController {

    @Autowired
    private GymTrainerService gymTrainerService;

    @Autowired
    private GymBattleService gymBattleService;

    @Value("${spring.application.name:autoplayers}")
    private String applicationName;

    @Value("${server.port:8083}")
    private String serverPort;

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInstanceInfo() {
        Map<String, String> info = new HashMap<>();
        try {
            info.put("service", applicationName);
            info.put("hostname", InetAddress.getLocalHost().getHostName());
            info.put("ip", InetAddress.getLocalHost().getHostAddress());
            info.put("port", serverPort);
        } catch (UnknownHostException e) {
            info.put("error", "Could not retrieve host information");
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping("/trainers")
    public ResponseEntity<Map<String, GymTrainer>> getTrainers() {
        return ResponseEntity.ok(gymTrainerService.getAllTrainers());
    }

    @GetMapping("/trainers/{name}")
    public ResponseEntity<GymTrainer> getTrainer(@PathVariable String name) {
        try {
            GymTrainer trainer = gymTrainerService.getTrainer(name);
            return ResponseEntity.ok(trainer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/battle")
    public ResponseEntity<?> startBattle(@RequestBody BattleRequest request) {
        try {
            BattleResult result = gymBattleService.startBattle(request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/battles/{username}")
    public ResponseEntity<List<GymBattle>> getBattleHistory(@PathVariable String username) {
        List<GymBattle> battles = gymBattleService.getBattleHistory(username);
        return ResponseEntity.ok(battles);
    }

    @GetMapping("/battles/{username}/wins")
    public ResponseEntity<List<GymBattle>> getWonBattles(@PathVariable String username) {
        List<GymBattle> battles = gymBattleService.getWonBattles(username);
        return ResponseEntity.ok(battles);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "autoplayers"));
    }
}

