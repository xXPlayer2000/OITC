package me.devcode.oitc.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

@Getter
public class IngameCountdown extends BukkitRunnable {

    private int timer = 311;


    @Override
    public void run() {

        if (timer <= timer) {
            timer--;
            if(timer == 300) {
                OITC.getInstance().setGameStatus(GameStatus.INGAME);
                Bukkit.getOnlinePlayers().forEach(player -> {

                });
            }
            Bukkit.getOnlinePlayers().forEach(player -> {
                OITC.getInstance().getGameUtils().updateScoreboard(player);
                OITC.getInstance().getTitleAPI().sendActionbar(player, "§fKills §7» §e" + OITC.getInstance().getStatsOfTheGame().getKills(player) + " §7| Lives left §7» §e" + OITC.getInstance().getPlayerUtils().getPlayerLives(player));
            });
            if(timer == 0) {
                OITC.getInstance().getGameUtils().onWin(true);
                cancel();
            }
        }
    }

}
