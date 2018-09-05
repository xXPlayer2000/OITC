package me.devcode.oitc.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayerUtils {

    private List<Player> players = new ArrayList<>();
    private List<Player> specs = new ArrayList<>();
    private Map<Player, Integer> playerLives = new ConcurrentHashMap<>();
    private Map<UUID, PlayerManager> playerManager = new ConcurrentHashMap<>();

    public void setPlayerManager(UUID player) {
        playerManager.put(player, new PlayerManager(player));
    }

    public PlayerManager getPlayerManager(UUID player) {
        return playerManager.getOrDefault(player, new PlayerManager(player));
    }

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
