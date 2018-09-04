package me.devcode.oitc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        if(OITC.getInstance().getGameStatus() == GameStatus.LOBBY) {
            e.setQuitMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Quit", true));
            OITC.getInstance().getPlayerUtils().removePlayer(e.getPlayer());
            OITC.getInstance().getMapVoting().removeVoteByPlayer(e.getPlayer());
            return;
        }
        if(OITC.getInstance().getPlayerUtils().getPlayers().contains(e.getPlayer())) {
            e.setQuitMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Quit", true));
            OITC.getInstance().getPlayerUtils().removePlayer(e.getPlayer());
            BedWars.getInstance().getPlayerManager().addDeaths(e.getPlayer().getUniqueId(), 1);
            BedWars.getInstance().getPlayerManager().setDataBack(e.getPlayer().getUniqueId());
            Bukkit.getOnlinePlayers().forEach(all ->
                    BedWars.getInstance().getGameUtils().updateScoreboard(all));
            BedWars.getInstance().getGameUtils().detectTeamDestroyed(team);
        }
    }

}
