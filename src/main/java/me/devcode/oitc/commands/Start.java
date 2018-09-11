package me.devcode.oitc.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.GameStatus;

public class Start implements CommandExecutor{
	
	
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
		if(OITC.getInstance().getLobbyCountdown().getTimer() <= 10) {

			return true;
		}
		player.sendMessage(OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Start", true));
		OITC.getInstance().getLobbyCountdown().setTimer(11);
		return true;
		
		
	}

}
