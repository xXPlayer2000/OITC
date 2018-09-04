package me.devcode.oitc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.devcode.oitc.OITC;

public class PlayerManager {
    private Map<String, Object> values = new ConcurrentHashMap<>();
    private List<Player> players = new ArrayList<>();

    public Map<String, Object> getValues() {
        return this.values;
    }

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    public void addValue(String value) {
        this.values.put(value, 0);
    }

    public void removeValue(String value) {
        this.values.remove(value);
    }

    public void setValue(String key, Object value) {
        if (this.values.containsKey(key)) {
            this.values.replace(key, value);
        } else {
            this.values.put(key, value);
        }
    }

    public Object getValue(String key) {
        return this.values.get(key);
    }

    public void loadStats(String uuid) {
        OITC.getInstance().getDataValues().forEach(dValues ->{
            setValue(dValues, OITC.getInstance().getMySQLMethods().getValue(uuid, dValues));
        });
    }

    public void setDataback(String uuid) {
        if(Bukkit.getPlayer(UUID.fromString(uuid)) == null)
            return;
        OITC.getInstance().getMySQLMethods().setAllMethod("oitc", "uuid", uuid, getValue("kills"), getValue("deaths"), getValue("wins"), getValue("games"), getValue("points"));
    }


    public UUID getPlayer() {
        return this.player;
    }
}
