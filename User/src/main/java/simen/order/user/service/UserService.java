package simen.order.user.service;

import simen.order.user.model.User;
import simen.order.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @PostConstruct
    public void initDefaultUser() {
        User defaultUser = new User();
        defaultUser.setUsername("defaultUser");
        userRepository.save(defaultUser);
    }

    public User addToInventory(String username, int pokedexNumber) {
        User user = getUser(username);

        user.getInventory().merge(pokedexNumber, 1, Integer::sum);
        return userRepository.save(user);
    }

    //Vil bli brukt senere når vi har en liste over hele inventory, skal kunne klikke på hvilket som helst kort og selge det
    public User removeFromInventory(String username, int pokedexNumber) {
        User user = getUser(username);

        boolean existed = user.getInventory().computeIfPresent(pokedexNumber, (key, value) -> {
            if (value > 1) {
                return value - 1;
            } else {
                return null;
            }
        }) != null;

        if (!existed) {
            throw new RuntimeException("Cannot remove: Pokémon not in inventory");
        }

        return userRepository.save(user);
    }

    public Map<Integer, Integer> getPlayerInventory(String username) {
        return userRepository.findById(username)
                .map(User::getInventory)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public User sellCard(String username, int sellPrice) {
        User user = getUser(username);
        user.setCoins(user.getCoins() + sellPrice);

        return userRepository.save(user);
    }

    public int getUserCoins(String username) {
        User user = getUser(username);
        return user.getCoins();
    }

    public User getUser(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
        return user;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}