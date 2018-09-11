package me.devcode.oitc.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import me.devcode.oitc.OITC;
import me.devcode.oitc.utils.PlayerManager;

public class Stats implements CommandExecutor{

    @Override
    public boolean onCommand(final CommandSender cs, Command cmd, String label,
                             String[] args) {
        if(!(cs instanceof Player)) {
            return true;
        }
        if(args.length == 0) {

            Player p = (Player) cs;
            PlayerManager playerManager = OITC.getInstance().getPlayerUtils().getPlayerManager(p.getUniqueId());

            int kills = (int) playerManager.getValue("kills");
            if (kills < 0) {
                kills = 0;
            }
            int deaths = (int) playerManager.getValue("deaths");
            if (deaths < 0) {
                deaths = 0;
            }
            int wins = (int) playerManager.getValue("wins");
            int points = (int) playerManager.getValue("points");
            int games = (int) playerManager.getValue("games");
            double kd = Double.valueOf(kills) / Double.valueOf(deaths);
            if (deaths == 0) {
                kd = kills;
            }

            DecimalFormat f = new DecimalFormat("#0.00");
            double toFormat = ((double) Math.round(kd * 100)) / 100;

            String formatted = f.format(toFormat);
            cs.sendMessage("");
            cs.sendMessage("§7Kills §8» §e" + kills);
            cs.sendMessage("§7Deaths §8» §e" + deaths);
            cs.sendMessage("§7K/D §8» §e" + formatted.replace("NaN", "0").replace("Infinity", "0"));
            cs.sendMessage("§7Points §8» §e" + points);
            cs.sendMessage("§7Wins §8» §e" + wins);
            cs.sendMessage("§7Games §8» §e" + games);
            cs.sendMessage("§7Rank §8» §e" + playerManager.getValue("rank"));
            cs.sendMessage("");

            return true;
        }
        if(Bukkit.getPlayer(args[0]) != null) {
            Player p = Bukkit.getPlayer(args[0]);
            PlayerManager playerManager = OITC.getInstance().getPlayerUtils().getPlayerManager(p.getUniqueId());

            int kills = (int) playerManager.getValue("kills");
            if (kills < 0) {
                kills = 0;
            }
            int deaths = (int) playerManager.getValue("deaths");
            if (deaths < 0) {
                deaths = 0;
            }
            int wins = (int) playerManager.getValue("wins");
            int points = (int) playerManager.getValue("points");
            int games = (int) playerManager.getValue("games");
            double kd = Double.valueOf(kills) / Double.valueOf(deaths);
            if (deaths == 0) {
                kd = kills;
            }

            DecimalFormat f = new DecimalFormat("#0.00");
            double toFormat = ((double) Math.round(kd * 100)) / 100;

            String formatted = f.format(toFormat);
            cs.sendMessage("");
            cs.sendMessage("§7Kills §8» §e" + kills);
            cs.sendMessage("§7Deaths §8» §e" + deaths);
            cs.sendMessage("§7K/D §8» §e" + formatted.replace("NaN", "0").replace("Infinity", "0"));
            cs.sendMessage("§7Points §8» §e" + points);
            cs.sendMessage("§7Wins §8» §e" + wins);
            cs.sendMessage("§7Games §8» §e" + games);
            cs.sendMessage("§7Rank §8» §e" + playerManager.getValue("rank"));
            cs.sendMessage("");
            return true;
        }else{
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            if(!OITC.getInstance().getStats().getBooleanMethod("oitc", "uuid", p.getUniqueId().toString())) {
            return true;
            }

            int kills = (int) OITC.getInstance().getMySQLMethods().getValue(p.getUniqueId().toString(), "kills");
            int deaths =(int) OITC.getInstance().getMySQLMethods().getValue(p.getUniqueId().toString(), "deaths");
            int wins = (int) OITC.getInstance().getMySQLMethods().getValue(p.getUniqueId().toString(), "wins");
            int games = (int) OITC.getInstance().getMySQLMethods().getValue(p.getUniqueId().toString(), "games");
            int points = (int) OITC.getInstance().getMySQLMethods().getValue(p.getUniqueId().toString(), "points");
            double kd = Double.valueOf(kills) / Double.valueOf(deaths);
            if(deaths == 0) {
                kd = kills;
            }

            DecimalFormat f = new DecimalFormat("#0.00");
            double toFormat = ((double)Math.round(kd*100))/100;

            String formatted = f.format(toFormat);
            cs.sendMessage("");
            cs.sendMessage("§7Kills §8» §e" + kills);
            cs.sendMessage("§7Deaths §8» §e" + deaths);
            cs.sendMessage("§7K/D §8» §e" + formatted.replace("NaN", "0").replace("Infinity", "0"));
            cs.sendMessage("§7Points §8» §e" + points);
            cs.sendMessage("§7Wins §8» §e" + wins);
            cs.sendMessage("§7Games §8» §e" + games);
            cs.sendMessage("§7Rank §8» §e" + OITC.getInstance().getMySQLMethods().getRank("oitc", p.getUniqueId().toString()));
            cs.sendMessage("");

            return true;


        }
    }
}
