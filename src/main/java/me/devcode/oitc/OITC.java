package me.devcode.oitc;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.devcode.oitc.countdowns.EndCountdown;
import me.devcode.oitc.countdowns.IngameCountdown;
import me.devcode.oitc.countdowns.LobbyCountdown;
import me.devcode.oitc.listeners.CancelListeners;
import me.devcode.oitc.listeners.DeathListener;
import me.devcode.oitc.listeners.JoinListener;
import me.devcode.oitc.listeners.PreLoginListener;
import me.devcode.oitc.listeners.QuitListener;
import me.devcode.oitc.listeners.VotingListener;
import me.devcode.oitc.mysql.AsyncMySQL;
import me.devcode.oitc.mysql.MySQLMethods;
import me.devcode.oitc.mysql.MySQLStats;
import me.devcode.oitc.utils.GameStatus;
import me.devcode.oitc.utils.GameUtils;
import me.devcode.oitc.utils.MapVoting;
import me.devcode.oitc.utils.MessageUtils;
import me.devcode.oitc.utils.PlayerUtils;
import me.devcode.oitc.utils.StatsOfTheGame;
import me.devcode.oitc.utils.LocationUtils;
import me.devcode.oitc.utils.TitleAPI;

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
    private LobbyCountdown lobbyCountdown;
    private IngameCountdown ingameCountdown;
    private EndCountdown endCountdown;
    private GameUtils gameUtils;
    private LocationUtils locationUtils;
    private TitleAPI titleAPI;

    private List<String> dataValues = new ArrayList<>();
    private AsyncMySQL mysql;
    private MySQLStats stats;
    private MySQLMethods mySQLMethods;

    private File gameFile = new File("plugins/OITC", "game.yml");
    private FileConfiguration gameConfig;
    private File mysqlFile = new File("plugins/OITC", "mysql.yml");
    private FileConfiguration mysqlCfg = null;

    //Variables
    private int minPlayers = 2;
    private String mapName;
    private List<String> maps = new ArrayList<>();
    private boolean isForceMap = false;

    @Override
    public void onEnable() {
    instance = this;
    getDataFolder().mkdir();
    playerUtils = new PlayerUtils();
    gameStatus = GameStatus.LOBBY;
    messageUtils = new MessageUtils();
    mapVoting = new MapVoting();
    statsOfTheGame = new StatsOfTheGame();
    lobbyCountdown = new LobbyCountdown();
    gameUtils = new GameUtils();
    locationUtils = new LocationUtils();
    ingameCountdown = new IngameCountdown();
    endCountdown = new EndCountdown();
    titleAPI = new TitleAPI();
    setVariables();
    setMySQLConnection();
    registerListeners();
    registerCommands();
    }

    private void setVariables() {
        if (!gameFile.exists())
            loadFile("game.yml");
        if(!mysqlFile.exists())
            loadFile("mysql.yml");
        gameConfig = YamlConfiguration.loadConfiguration(gameFile);
        mysqlCfg = YamlConfiguration.loadConfiguration(mysqlFile);
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

    private void setMySQLConnection() {
        dataValues.add("kills");
        dataValues.add("deaths");
        dataValues.add("wins");
        dataValues.add("games");
        dataValues.add("points");

        mysql = new AsyncMySQL(this, mysqlCfg.getString("MySQL.Host"), mysqlCfg.getInt("MySQL.Port"), mysqlCfg.getString("MySQL.User"), mysqlCfg.getString("MySQL.Password"), mysqlCfg.getString("MySQL.Database"));
        mysql.update("CREATE TABLE IF NOT EXISTS oitc(uuid varchar(36), kills int, deaths int, wins int, games int, points int);");
        stats = new MySQLStats();
        mySQLMethods = new MySQLMethods();
    }

    private void registerCommands(){
        getDescription().getCommands().entrySet().stream().map(Map.Entry::getKey)
                .forEach( commandName ->{
                    try {
                        CommandExecutor commandExecutor = (CommandExecutor) Class.forName("me.devcode.oitc.commands." + StringUtils.capitalize(commandName)).getConstructor().newInstance();
                        register(commandName, commandExecutor);
                    } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                });
    }
    private Map<String, CommandExecutor> commands = new ConcurrentHashMap<>();

    private void register(String key, CommandExecutor val){
        commands.put(key, val);
        getCommand(key).setExecutor(val);
    }

    /*
    Registering all Listeners via list
     */
    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        List<Listener> listeners = new ArrayList<>();
        listeners.add(new JoinListener());
        listeners.add(new PreLoginListener());
        listeners.add(new QuitListener());
        listeners.add(new CancelListeners());
        listeners.add(new VotingListener());
        listeners.add(new DeathListener());
        listeners.forEach(listener ->
                pluginManager.registerEvents(listener, this));
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
