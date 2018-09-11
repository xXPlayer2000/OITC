package me.devcode.oitc.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;
import me.devcode.oitc.utils.ItemBuilder;

public class VotingListener implements Listener{

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (OITC.getInstance().getGameStatus() == GameStatus.LOBBY) {
            if (e.getItem() == null) {
                return;
            }
            if (e.getItem().getType() == Material.NETHER_STAR) {
                Inventory inv = Bukkit.createInventory(null, 9, "§aVoting");
                inv.setItem(4, new ItemBuilder(Material.MAP).setName("§3Map Voting").toItemStack());
                player.openInventory(inv);
                return;
            }
        }
    }

    @EventHandler
    public void onMapVoting(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (OITC.getInstance().getGameStatus() == GameStatus.LOBBY) {
            if(e.getCurrentItem() == null) {
                return;
            }
            if(e.getCurrentItem().getItemMeta() == null) {
                return;
            }
            if(e.getClickedInventory().getName() == null) {
                return;
            }
            if(e.getClickedInventory().getName().contains("§aVoting")) {
                if(e.getCurrentItem().getType() == Material.MAP) {
                    Inventory inv = Bukkit.createInventory(null, 18, "§aMap Voting");
                    IntStream.range(0, OITC.getInstance().getMaps().size()).forEach(i -> {
                        String name = OITC.getInstance().getMaps().get(i);
                        inv.setItem(i, new ItemBuilder(Material.MAP).setName("§a" + name).setLore("§7Votes: §a" + OITC.getInstance().getMapVoting().getVotes(name)).toItemStack());
                    });
                    player.openInventory(inv);
                    return;
                }
            }
            if(e.getClickedInventory().getName().contains("§aMap Voting")) {
                if(OITC.getInstance().getMapVoting().isOver()) {
                    player.closeInventory();
                    player.sendMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.VotingIsOver", true));
                    return;
                }
                String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                OITC.getInstance().getMapVoting().setVoteByPlayer(player, name);
                IntStream.range(0, OITC.getInstance().getMaps().size()).forEach(i -> {
                    String mapName = OITC.getInstance().getMaps().get(i);
                    e.getClickedInventory().setItem(i, new ItemBuilder(Material.MAP).setName("§a" + mapName).setLore("§7Votes: §a" + OITC.getInstance().getMapVoting().getVotes(mapName)).toItemStack());
                });
                return;
            }
        }
    }


}
