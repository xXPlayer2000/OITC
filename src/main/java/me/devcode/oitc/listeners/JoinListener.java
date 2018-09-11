package me.devcode.oitc.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ietf.jgss.Oid;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;
import me.devcode.oitc.utils.HoloAPi;
import me.devcode.oitc.utils.Hologram;
import me.devcode.oitc.utils.PlayerManager;

public class JoinListener implements Listener {
    private Random random = new Random();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //Adding Specater
        e.setJoinMessage(null);
        Player player = e.getPlayer();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setExp(0);
        player.setLevel(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.getActivePotionEffects().forEach(potionEffect ->
                player.removePotionEffect(potionEffect.getType()));
    if(OITC.getInstance().getGameStatus() != GameStatus.LOBBY) {
        OITC.getInstance().getPlayerUtils().addSpec(e.getPlayer());
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(OITC.getInstance().getPlayerUtils().getPlayers().get(random.nextInt(OITC.getInstance().getPlayerUtils().getPlayers().size())));
        return;
    }
    e.setJoinMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Join", true).replace("%PLAYER%", player.getName()));
    OITC.getInstance().getPlayerUtils().addPlayer(player);

        OITC.getInstance().getGameUtils().sendCustomScoreboard(player);
        PlayerManager playerManager = OITC.getInstance().getPlayerUtils().getPlayerManager(player.getUniqueId());
        int kills = (int) playerManager.getValue("kills");
        if (kills < 0) {
            kills = 0;
        }
        int deaths = (int) playerManager.getValue("deaths");
        if (deaths < 0) {
            deaths = 0;
        }
        int wins = (int) playerManager.getValue("wins");
        int points = (int) playerManager.getValue("points");
        int games = (int) playerManager.getValue("games");
        double kd = Double.valueOf(kills) / Double.valueOf(deaths);
        if (deaths == 0) {
            kd = kills;
        }

        DecimalFormat f = new DecimalFormat("#0.00");
        double toFormat = ((double) Math.round(kd * 100)) / 100;

        String formatted = f.format(toFormat);
        List<String> lines = new ArrayList<>();
        lines.add("§7Your §6Stats");
        lines.add("§7Your Rank: §6" + playerManager.getValue("rank"));
        lines.add("§7Kills: §6" + kills);
        lines.add("§7Deaths: §6" + deaths);
        lines.add("§7K/D: §6" + formatted);
        lines.add("§7Games: §6" + games);
        lines.add("§7Wins: §6" + wins);
        lines.add("§7Points: §6" + points);

        HoloAPi api = new HoloAPi(OITC.getInstance().getLocationUtils().getHologramLocation(), lines);
        api.disp(player);
    if(Bukkit.getOnlinePlayers().size() >= OITC.getInstance().getMinPlayers() &&!OITC.getInstance().getLobbyCountdown().isStarted()) {
        OITC.getInstance().getLobbyCountdown().setStarted(true);
        OITC.getInstance().getLobbyCountdown().runTaskTimer(OITC.getInstance(), 0,20);
    }

    }

}
