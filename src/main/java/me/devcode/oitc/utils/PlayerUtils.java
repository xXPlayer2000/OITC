package me.devcode.oitc.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import lombok.Getter;

@Getter
public class PlayerUtils {

    private List<Player> players = new ArrayList<>();
    private List<Player> specs = new ArrayList<>();
    private Map<Player, Integer> playerLives = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addSpec(Player player) {
        specs.add(player);
    }

    public void removeSpec(Player player) {
        specs.remove(player);
    }

    public Integer getLives(Player player) {
        return playerLives.getOrDefault(player, 0);
    }

}
