package me.devcode.oitc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        e.getDrops().clear();
        e.setKeepInventory(false);
        if (OITC.getInstance().getPlayerUtils().getPlayers().contains(player)) {
            OITC.getInstance().getPlayerUtils().removePlayerLive(player);
            OITC.getInstance().getPlayerUtils().getPlayerManager(player.getUniqueId()).addValue("deaths", 1);
            if (e.getEntity().getKiller() != null) {
                    if(OITC.getInstance().getPlayerUtils().getPlayerLives(player) == 0) {
                        OITC.getInstance().getTitleAPI().send(player, OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Eliminated", false), "",1,1,1);
                    }


                OITC.getInstance().getPlayerUtils().getPlayerManager(player.getKiller().getUniqueId()).addValue("kills", 1);

                OITC.getInstance().getStatsOfTheGame().addKill(player.getKiller());
                player.getKiller().playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_DETACH, 1,1);
                OITC.getInstance().getTitleAPI().send(player.getKiller(), OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Kill", false), "",1,1,1);
            } else{
                if(OITC.getInstance().getPlayerUtils().getPlayerLives(player) == 0) {
                    OITC.getInstance().getPlayerUtils().getPlayerManager(player.getUniqueId()).addValue("deaths", 1);
                }
            }
            player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1,1);
            e.setDeathMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Death", true).replace("%PLAYER%", player.getName()));
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.spigot().respawn();
                }
            }.runTaskLater(OITC.getInstance(), 10);
            
    
            return;
        }
    }

    private Random random = new Random();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        try {
            if (OITC.getInstance().getGameStatus() == GameStatus.INGAME) {
                    if (OITC.getInstance().getPlayerUtils().getPlayers().contains(e.getPlayer())) {

                        if (OITC.getInstance().getPlayerUtils().getPlayerLives(e.getPlayer()) == 0) {
                            OITC.getInstance().getPlayerUtils().removePlayer(e.getPlayer());
                            OITC.getInstance().getPlayerUtils().addSpec(e.getPlayer());
                            e.getPlayer().setGameMode(GameMode.SPECTATOR);
                            e.setRespawnLocation(OITC.getInstance().getPlayerUtils().getPlayers().get(random.nextInt(OITC.getInstance().getPlayerUtils().getPlayers().size())).getLocation());
                            Bukkit.getOnlinePlayers().forEach(all -> {
                                all.hidePlayer(e.getPlayer());
                            });
                            if(OITC.getInstance().getPlayerUtils().getPlayers().size() == 1) {
                                OITC.getInstance().getGameUtils().onWin(false);
                            }
                        }else{
                            OITC.getInstance().getLobbyCountdown().setInventory(e.getPlayer());
                            e.setRespawnLocation(OITC.getInstance().getLocationUtils().teleportRandom(e.getPlayer()));
                        }
                    }


                
            }
        }catch(Exception e1) {

        }
    }



}
