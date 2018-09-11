package me.devcode.oitc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.devcode.oitc.OITC;

public class PlayerManager {
    private UUID player;
    private boolean isDataBack = false;
    private Map<String, Object> values = new ConcurrentHashMap<>();

    public PlayerManager(UUID player) {
        this.player = player;
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    public void addValue(String key, Integer value) {
        this.values.put(key, (Integer) this.values.getOrDefault(key, 0)+value);
    }

    public void removeValue(String value) {
        this.values.remove(value);
    }

    public void setValue(String key, Object value) {
       this.values.put(key, value);

    }

    public Object getValue(String key) {
        return this.values.getOrDefault(key, null);
    }

    public UUID getPlayer() {
        return this.player;
    }

    public void loadStats(String uuid) {
        OITC.getInstance().getDataValues().forEach(dValues ->{
            setValue(dValues, OITC.getInstance().getMySQLMethods().getValue(uuid, dValues));
        });
        setValue("rank", OITC.getInstance().getMySQLMethods().getRank("oitc", uuid));

    }



    public void setDataback() {
        if(Bukkit.getPlayer(UUID.fromString(player.toString())) == null)
            return;
        if(isDataBack)
            return;
        isDataBack = true;
        OITC.getInstance().getMySQLMethods().setAllMethod("oitc", "uuid", player.toString(), getValue("kills"), getValue("deaths"), getValue("wins"), getValue("games"), getValue("points"));

    }
}
