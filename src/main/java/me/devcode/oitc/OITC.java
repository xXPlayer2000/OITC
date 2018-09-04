package me.devcode.oitc;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.devcode.oitc.mysql.AsyncMySQL;
import me.devcode.oitc.mysql.MySQLMethods;
import me.devcode.oitc.mysql.MySQLStats;
import me.devcode.oitc.utils.GameStatus;
import me.devcode.oitc.utils.MapVoting;
import me.devcode.oitc.utils.MessageUtils;
import me.devcode.oitc.utils.PlayerManager;
import me.devcode.oitc.utils.PlayerUtils;
import me.devcode.oitc.utils.StatsOfTheGame;

@Getter
@Setter
public class OITC extends JavaPlugin {
    @Getter
    private static OITC instance;
    private PlayerUtils playerUtils;
    private GameStatus gameStatus;
    private MessageUtils messageUtils;
    private MapVoting mapVoting;
    private StatsOfTheGame statsOfTheGame;

    private List<String> dataValues = new ArrayList<>();
    private AsyncMySQL mysql;
    private MySQLStats stats;
    private MySQLMethods mySQLMethods;
    private PlayerManager playerManager;

    private File gameFile = new File("plugins/OITC", "game.yml");
    private FileConfiguration gameConfig;

    //Variables
    private int minPlayers = 2;
    private String mapName;
    private List<String> maps = new ArrayList<>();

    @Override
    public void onEnable() {
    instance = this;
    playerUtils = new PlayerUtils();
    gameStatus = GameStatus.LOBBY;
    playerManager = new PlayerManager();
    messageUtils = new MessageUtils();
    mapVoting = new MapVoting();
    statsOfTheGame = new StatsOfTheGame();
    setVariables();
    }

    private void setVariables() {
        if (!gameFile.exists())
            loadFile("game.yml");
        gameConfig = YamlConfiguration.loadConfiguration(gameFile);
        maps = gameConfig.getStringList("Game.Worlds");
        maps.forEach(s -> {
            WorldCreator worldCreator = new WorldCreator(s);
            World world = worldCreator.createWorld();
            world.setDifficulty(Difficulty.EASY);
            world.setWeatherDuration(0);
            world.setThunderDuration(0);
            world.setTime(1000);
            world.setThundering(false);
            world.setStorm(false);
            world.getEntities().forEach(entity -> {
                if (!(entity instanceof Player)) {
                    entity.remove();
                }
            });
        });
         minPlayers = Integer.valueOf(gameConfig.getString("Game.MinPlayers").replace("[", "").replace("]", ""));
        mapName = gameConfig.getStringList("Game.Map").get(0);
    }

    /*
    Registering all Listeners via list
     */
    private void registerListeners() {

    }

    /*
    Method from the Internet
     */
    @SneakyThrows
    public void loadFile(String file) {
        File t = new File(this.getDataFolder(), file);
        System.out.println("Writing new file: " + t.getAbsolutePath());

        t.createNewFile();
        FileWriter out = new FileWriter(t);
        InputStream is = getClass().getResourceAsStream("/" + file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            out.write(line + "\n");
        }
        System.out.println(line);
        out.flush();
        is.close();
        isr.close();
        br.close();
        out.close();

    }

}
