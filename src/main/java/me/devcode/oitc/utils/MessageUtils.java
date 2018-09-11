package me.devcode.oitc.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import me.devcode.oitc.OITC;

public class MessageUtils {

    private File file = new File("plugins/OITC", "messages.yml");
    private FileConfiguration cfg = null;
    public MessageUtils() {
        if(!file.exists()) {
            OITC.getInstance().loadFile("messages.yml");
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessageByConfig(String path, boolean prefix) {
        if(!cfg.contains(path)) {
            cfg.set(path, "test");
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "a";
        }
        if(prefix)
            return cfg.getString("Messages.Prefix").replace("&", "§")+""+cfg.getString(path).replace("&", "§");
        return cfg.getString(path).replace("&", "§");
        /*
        if(prefix)
            return cfg.getString("Messages.Prefix").replace("&", "§")+""+cfg.getString(path).replace("&", "§");
        return cfg.getString(path).replace("&", "§");
        */
    }

}
