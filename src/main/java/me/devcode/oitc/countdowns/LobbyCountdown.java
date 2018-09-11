package me.devcode.oitc.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

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
                    Bukkit.getOnlinePlayers().forEach(player -> {
                                OITC.getInstance().getGameUtils().sendCustomScoreboard(player);
                                player.setLevel(0);
                                player.setExp(0);
                            });
                    cancel();
                }

            } else {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    OITC.getInstance().getGameUtils().sendCustomScoreboard(player);

                        player.setLevel(timer);
                        player.setExp((float) timer / 60);
                });
                if(timer == 60 || timer == 30 || timer == 10 || timer <= 5 && timer > 0) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0, 4);

                    });


                }
                if (timer == 5) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if (!OITC.getInstance().isForceMap()) {
                            OITC.getInstance().getMapVoting().handleVoting();
                        }
                        OITC.getInstance().getTitleAPI().send(player, "§6Map: §b"+OITC.getInstance().getMapName(), "", 1,1,1);
                    });
                    OITC.getInstance().getMaps().forEach(maps -> {
                        if (!maps.equalsIgnoreCase(OITC.getInstance().getMapName()))
                            Bukkit.unloadWorld(maps, false);
                    });
                }
                if(timer == 0) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.setFoodLevel(22);
                        player.setLevel(0);
                        player.setExp(0);
                        player.getInventory().clear();
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        player.getInventory().setArmorContents(null);
                        player.sendMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.GameIsStarting", true));
                        OITC.getInstance().getStatsOfTheGame().loadPlayers(player);
                        OITC.getInstance().getPlayerUtils().setPlayerLives(player);
                        OITC.getInstance().getLocationUtils().teleportRandom(player);
                        setInventory(player);
                    });
                    OITC.getInstance().setGameStatus(GameStatus.WARMUP);
                    OITC.getInstance().getIngameCountdown().runTaskTimer(OITC.getInstance(), 0,20);
                    cancel();
                }
            }
        }
    }

    public void setInventory(Player player) {
        player.getInventory().setItem(0, new ItemStack(Material.WOOD_SWORD));
        player.getInventory().setItem(1, new ItemStack(Material.BOW));
        player.getInventory().setItem(7, new ItemStack(Material.REDSTONE, OITC.getInstance().getPlayerUtils().getPlayerLives(player)));
        player.getInventory().setItem(8, new ItemStack(Material.ARROW));
    }

}
