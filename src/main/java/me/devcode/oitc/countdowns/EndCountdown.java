package me.devcode.oitc.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.Getter;
import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

@Getter
public class EndCountdown extends BukkitRunnable {

    int timer = 11;
    @Override
    public void run() {
        Queue<UUID> globalQueue = new ConcurrentLinkedQueue<>();

            OITC.getInstance().getPlayerUtils().getPlayerManager().keySet().forEach(uuid ->
                    globalQueue.add(uuid));
            globalQueue.forEach(uuid -> {
                        OITC.getInstance().getPlayerUtils().getPlayerManager(uuid).setDataback();
                        globalQueue.remove(uuid);
                    });
            OITC.getInstance().setGameStatus(GameStatus.END);

                    // TODO Auto-generated method stub
                    if (timer <= timer) {
                        timer--;
                    }
                    if (timer == 10 || timer <= 5 && timer > 0) {
                        int finalTimer = timer;
                        Bukkit.getOnlinePlayers().forEach(all ->
                            all.sendMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Restart", true).replace("%TIMER%", String.valueOf(finalTimer))));
                    }
                    if(timer == 1) {
                        OITC.getInstance().getMysql().getMySQL().closeConnection();
                    }
                    if (timer == 0) {

                        Bukkit.shutdown();
                    }
                }
}
