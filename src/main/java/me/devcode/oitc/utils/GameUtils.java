package me.devcode.oitc.utils;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.devcode.oitc.OITC;

public class GameUtils {

    public void sendCustomScoreboard(Player player) {
        //Packets so you can use your own tab at the beginning
        CraftPlayer craftPlayer = (CraftPlayer) player;

        net.minecraft.server.v1_8_R3.Scoreboard scoreboard = new net.minecraft.server.v1_8_R3.Scoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.registerObjective("abc", IScoreboardCriteria.b);
        scoreboardObjective.setDisplayName("§e»  §3OITC  §e«");

        ScoreboardScore mapScore = new ScoreboardScore(scoreboard, scoreboardObjective, "§6Map§8:");
        mapScore.setScore(4);
        ScoreboardScore mapScore2 = new ScoreboardScore(scoreboard, scoreboardObjective, "§b  Voting...");
        mapScore2.setScore(3);
        ScoreboardScore placer = new ScoreboardScore(scoreboard, scoreboardObjective, "§c");
        placer.setScore(2);
        ScoreboardScore countdownScore = null;
        ScoreboardScore countdownScore2 = null;
        if(OITC.getInstance().getLobbyCountdown().isStarted()) {
            countdownScore = new ScoreboardScore(scoreboard, scoreboardObjective, "§eGame starts in§8:");
            countdownScore.setScore(1);
            int totalSecs = OITC.getInstance().getLobbyCountdown().getTimer();
            int minutes =(totalSecs %3600)/60;
            int seconds = totalSecs %60;

            String timeString =String.format("%02d:%02d", minutes, seconds);
            countdownScore2 = new ScoreboardScore(scoreboard, scoreboardObjective, "§f"+timeString);
            countdownScore2.setScore(0);
        }else{
            countdownScore = new ScoreboardScore(scoreboard, scoreboardObjective, "§c§lWaiting for players...");
            countdownScore.setScore(1);
        }
        PacketPlayOutScoreboardObjective removeCurrentScoreboardObjectivePacket = new PacketPlayOutScoreboardObjective(scoreboardObjective, 1);
        PacketPlayOutScoreboardObjective createNewScoreboardObjectivePacket = new PacketPlayOutScoreboardObjective(scoreboardObjective, 0);
        PacketPlayOutScoreboardDisplayObjective displayScoreboardObject = new PacketPlayOutScoreboardDisplayObjective(1, scoreboardObjective);

        PacketPlayOutScoreboardScore map1 = new PacketPlayOutScoreboardScore(mapScore);
        PacketPlayOutScoreboardScore map2 = new PacketPlayOutScoreboardScore(mapScore2);
        PacketPlayOutScoreboardScore placer2 = new PacketPlayOutScoreboardScore(placer);
        PacketPlayOutScoreboardScore countdown = new PacketPlayOutScoreboardScore(countdownScore);

        craftPlayer.getHandle().playerConnection.sendPacket(removeCurrentScoreboardObjectivePacket);
        craftPlayer.getHandle().playerConnection.sendPacket(createNewScoreboardObjectivePacket);
        craftPlayer.getHandle().playerConnection.sendPacket(displayScoreboardObject);

        craftPlayer.getHandle().playerConnection.sendPacket(map1);
        craftPlayer.getHandle().playerConnection.sendPacket(map2);
        craftPlayer.getHandle().playerConnection.sendPacket(placer2);
        craftPlayer.getHandle().playerConnection.sendPacket(countdown);
        if(countdownScore2 != null) {
            PacketPlayOutScoreboardScore countdown2 = new PacketPlayOutScoreboardScore(countdownScore2);
            craftPlayer.getHandle().playerConnection.sendPacket(countdown2);
        }
    }

}
