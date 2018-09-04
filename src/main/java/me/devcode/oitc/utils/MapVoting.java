package me.devcode.oitc.utils;

import net.aiohub.bedwars.BedWars;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class MapVoting {

    private HashMap<Player, String> getVoteByPlayer = new HashMap<>();
    private HashMap<String, Integer> getVotes = new HashMap<>();
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
        if (!getVoteByPlayer.containsKey(player)) {

            return null;
        }
        return getVoteByPlayer.get(player);
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
        if (getVotes.containsKey(name)) {

            getVotes.put(name, getVotes.get(name) + vote);
        } else {
            getVotes.put(name, vote);
        }
    }

    public int getVotes(String name) {
        if (!getVotes.containsKey(name)) {
            return 0;
        }
        return getVotes.get(name);
    }

    public void handleVoting() {
        try {
            int maxValueInMap = (Collections.max(getVotes.values()));
            for (Map.Entry<String, Integer> entry : getVotes.entrySet()) {
                if (entry.getValue() == maxValueInMap) {
                    BedWars.getInstance().setMapName(entry.getKey());
                    break;
                }
            }
            isOver = true;
        }catch(Exception e) {

        }
    }


}
