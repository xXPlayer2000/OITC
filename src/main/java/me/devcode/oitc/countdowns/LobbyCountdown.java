package me.devcode.oitc.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devcode.oitc.OITC;

public class LobbyCountdown extends ModdableRunnable{

    public int timer = 61;
    private boolean started = false;
    @Override
    public void run() {
        if(timer <= timer) {
            timer--;
            if (Bukkit.getOnlinePlayers().size() < OITC.getInstance().getMinPlayers()) {
                if(started) {
                    started = false;
                    timer = 61;
                    countdown = 5;
                    cancel();
                    this.runTaskTimer(UHCRun.getInstance(), 0, countdown);
                }
                Bukkit.getOnlinePlayers().forEach(all -> {
                    all.setExp(0);
                    all.setLevel(0);
                    int playersLeft = UHCRun.getInstance().getMinPlayers()-Bukkit.getOnlinePlayers().size();
                    if(playersLeft > 1)
                        TitleApi.endTitel(all, "§7Es fehlen noch §b" + playersLeft + " §7Spieler.");
                    else
                        TitleApi.endTitel(all, "§7Es fehlt noch §b" + playersLeft + " §7Spieler.");
                });
                timer = 61;
                started = false;
                return;
            }else{
                setStarted(true);
                countdown = 20;
                cancel();
                this.runTaskTimer(UHCRun.getInstance(), 0, countdown);
            }
            for(Player all : Bukkit.getOnlinePlayers()) {
                TitleApi.endTitel(all, "§7Das Spiel startet in §b" + timer);
            }
        }
    }
