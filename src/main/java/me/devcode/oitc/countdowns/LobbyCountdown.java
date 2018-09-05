package me.devcode.oitc.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import me.devcode.oitc.OITC;

@Getter
@Setter
public class LobbyCountdown extends BukkitRunnable {

    private int timer = 61;
    private boolean started = false;

    @Override
    public void run() {

        if (timer <= timer) {
            timer--;
            if (Bukkit.getOnlinePlayers().size() < OITC.getInstance().getMinPlayers()) {
                if (started) {
                    started = false;
                    timer = 61;
                    Bukkit.getOnlinePlayers().forEach(player ->
                    OITC.getInstance().getGameUtils().sendCustomScoreboard(player));
                    cancel();
                }

            } else {
                Bukkit.getOnlinePlayers().forEach(player ->
                    OITC.getInstance().getGameUtils().sendCustomScoreboard(player));
                if(timer == 60 || timer == 30 || timer == 10 || timer <= 5 && timer > 0) {
                    Bukkit.getOnlinePlayers().forEach(player ->
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 0,4));


                }
                if(timer == 0) {

                }
            }
        }
    }
}
