package me.devcode.oitc.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

public class JoinListener implements Listener {
    private Random random = new Random();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //Adding Specater
        e.setJoinMessage(null);
        Player player = e.getPlayer();
        player.setExp(0);
        player.setLevel(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect ->
                player.removePotionEffect(potionEffect.getType()));
    if(OITC.getInstance().getGameStatus() != GameStatus.LOBBY) {
        OITC.getInstance().getPlayerUtils().addSpec(e.getPlayer());
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(OITC.getInstance().getPlayerUtils().getPlayers().get(random.nextInt(OITC.getInstance().getPlayerUtils().getPlayers().size())));
        return;
    }
    e.setJoinMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Join", true));
    OITC.getInstance().getPlayerUtils().addPlayer(player);

    }

}
