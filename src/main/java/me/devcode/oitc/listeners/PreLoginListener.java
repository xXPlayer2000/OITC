package me.devcode.oitc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

public class PreLoginListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        if (OITC.getInstance().getGameStatus() == GameStatus.LOBBY) {
            OITC.getInstance().getMySQLMethods().createPlayer(e.getUniqueId().toString());
            OITC.getInstance().getPlayerUtils().setPlayerManager(e.getUniqueId());
            OITC.getInstance().getPlayerUtils().getPlayerManager(e.getUniqueId()).loadStats(e.getUniqueId().toString());
        }
    }

}
