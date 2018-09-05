package me.devcode.oitc.utils;


import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;
import me.devcode.oitc.OITC;

public class MapVoting {

    private Map<Player, String> getVoteByPlayer = new ConcurrentHashMap<>();
    private Map<String, Integer> getVotes = new ConcurrentHashMap<>();
    @Setter
    @Getter
    private boolean isOver = false;



    public void setVoteByPlayer(Player player, String name) {

        if (getVoteByPlayer(player) != null) {
            if (getVoteByPlayer(player).contains(name)) {
                return;
            }

            setVote(getVoteByPlayer(player), -1);
        }
        getVoteByPlayer.put(player, name);
        setVote(name, 1);
    }

    public String getVoteByPlayer(Player player) {

        return getVoteByPlayer.getOrDefault(player, null);
    }

    public void removeVoteByPlayer(Player player) {
        if (getVoteByPlayer(player) != null) {

            setVote(getVoteByPlayer(player), -1);
            getVoteByPlayer.remove(player);
        }
    }

    public void setVote(String name, int vote) {

        if(name == null) {
            return;
        }
        getVotes.put(name, getVotes.getOrDefault(name, 0)+vote);
    }

    public int getVotes(String name) {

        return getVotes.getOrDefault(name,0);
    }

    public void handleVoting() {
        try {
            int maxValueInMap = (Collections.max(getVotes.values()));
            for (Map.Entry<String, Integer> entry : getVotes.entrySet()) {
                if (entry.getValue() == maxValueInMap) {
                    OITC.getInstance().setMapName(entry.getKey());
                    break;
                }
            }
            isOver = true;
        }catch(Exception e) {

        }
    }


}
