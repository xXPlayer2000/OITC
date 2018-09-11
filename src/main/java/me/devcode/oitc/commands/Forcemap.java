package me.devcode.oitc.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

public class Forcemap implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("oitc.admin")) {
        return true;
        }
        if(OITC.getInstance().getGameStatus() != GameStatus.LOBBY) {

            return true;
        }
        if(args.length != 1)
            return true;
        if(OITC.getInstance().getLobbyCountdown().getTimer() <= 5 || OITC.getInstance().getMapVoting().isOver()) {
            return true;
        }
        String map = args[0];
        if(!OITC.getInstance().getMaps().contains(map)) {
            return true;
        }
        player.sendMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Map", true).replace("%MAP%", player.getName()));
        OITC.getInstance().setMapName(map);
        OITC.getInstance().setForceMap(true);
        return true;


    }

}

