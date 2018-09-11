package me.devcode.oitc.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import me.devcode.oitc.OITC;

@Getter
public class StatsOfTheGame {

    private Map<Player, Integer> kills = new ConcurrentHashMap<>();
    private Map<Player, Integer> arrowShots = new ConcurrentHashMap<>();
    private Map<Player, Integer> hits = new ConcurrentHashMap<>();

    public void loadPlayers(Player player) {
        kills.put(player, 0);
        arrowShots.put(player, 0);
        hits.put(player, 0);
    }

    public void addKill(Player player) {
        if(!kills.containsKey(player)) {
            kills.put(player, 1);
            return;
        }
        kills.put(player, kills.get(player)+1);
    }

    public void addShot(Player player) {
        arrowShots.put(player, arrowShots.getOrDefault(player, 0)+1);
    }

    public void addHits(Player player) {
        hits.put(player, hits.getOrDefault(player, 0)+1);
    }

    public void sendMessage(Player player) {
        if(player == null) {
            return;
        }
        player.sendMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Statistics", false));
        player.sendMessage("§7Kills: §6" + getKills(player));
        player.sendMessage("§7Hits: §6" + getHits(player));
        player.sendMessage("§7Shots: §6" + getShots(player));
    }

    public Integer getKills(Player player) {
        if(!kills.containsKey(player)) {
           return 0;
        }
        return kills.get(player);
    }

    public Integer getHits(Player player) {
       return hits.getOrDefault(player, 0);
    }

    public Integer getShots(Player player) {
        return arrowShots.getOrDefault(player, 0);
    }

}
