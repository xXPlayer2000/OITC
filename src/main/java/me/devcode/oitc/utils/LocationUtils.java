package me.devcode.oitc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.devcode.oitc.OITC;

public class LocationUtils {

    private File file = new File("plugins/OITC", "locations.yml");
    private FileConfiguration cfg = null;
    private Random random = new Random();

    public LocationUtils() {
    if(!file.exists()) {
        OITC.getInstance().loadFile("locations.yml");
    }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void teleportToLobby(Player player) {
        String[] spawn = cfg.getString("Spawn.Lobby").split(";");
        World world = Bukkit.getWorld(spawn[0]);
        double x = Double.valueOf(spawn[1]);
        double y = Double.valueOf(spawn[2]);
        double z = Double.valueOf(spawn[3]);
        float yaw = Float.valueOf(spawn[4]);
        float pitch = Float.valueOf(spawn[5]);
        player.teleport(new Location(world, x, y, z, yaw, pitch));
    }

    public Location teleportRandom(Player player) {
        List<String> list = cfg.getStringList("Map.Locations."+OITC.getInstance().getMapName());
        String spawns = list.get(random.nextInt(list.size()));
        String[] spawn = spawns.split(";");
        World world = Bukkit.getWorld(spawn[0]);
        double x = Double.valueOf(spawn[1]);
        double y = Double.valueOf(spawn[2]);
        double z = Double.valueOf(spawn[3]);
        float yaw = Float.valueOf(spawn[4]);
        float pitch = Float.valueOf(spawn[5]);
        Location loc = new Location(world, x, y, z, yaw, pitch);
        player.teleport(loc);
        return loc;
    }

    public Location getHologramLocation() {

        if (cfg.getString("Stats.Hologram") == null) {

            return null;
        }
        String location = cfg.getString("Stats.Hologram");
        String[] arrayLocation = location.split(";");
        World world = Bukkit.getWorld(arrayLocation[0]);
        double x = Double.valueOf(arrayLocation[1]);
        double y =Double.valueOf(arrayLocation[2]);
        double z = Double.valueOf(arrayLocation[3]);
        double yaw = Double.valueOf(arrayLocation[4]);
        double pitch = Double.valueOf(arrayLocation[5]);
        return new Location(world, x, y, z);
    }

}
