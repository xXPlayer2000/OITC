package me.devcode.oitc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ietf.jgss.Oid;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        if(OITC.getInstance().getGameStatus() == GameStatus.LOBBY) {
            e.setQuitMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Quit", true).replace("%PLAYER%", e.getPlayer().getName()));
            OITC.getInstance().getPlayerUtils().removePlayer(e.getPlayer());
            OITC.getInstance().getMapVoting().removeVoteByPlayer(e.getPlayer());
            return;
        }
        if(OITC.getInstance().getPlayerUtils().getPlayers().contains(e.getPlayer())) {
            e.setQuitMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Quit", true).replace("%PLAYER%", e.getPlayer().getName()));
            OITC.getInstance().getPlayerUtils().removePlayer(e.getPlayer());
            OITC.getInstance().getPlayerUtils().getPlayerManager(e.getPlayer().getUniqueId()).addValue("deaths", 1);
            OITC.getInstance().getPlayerUtils().getPlayerManager(e.getPlayer().getUniqueId()).setDataback();
            if(OITC.getInstance().getPlayerUtils().getPlayers().size() == 1) {
                OITC.getInstance().getGameUtils().onWin(false);
            }
           // Bukkit.getOnlinePlayers().forEach(all ->
             //       BedWars.getInstance().getGameUtils().updateScoreboard(all));
           // BedWars.getInstance().getGameUtils().detectTeamDestroyed(team);
        }
    }

}
