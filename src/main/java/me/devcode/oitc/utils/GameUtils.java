package me.devcode.oitc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import me.devcode.oitc.OITC;

public class GameUtils {

    private HashMap<Player, Scoreboard> scoreboard2 = new HashMap<>();
    public void sendCustomScoreboard(Player player) {
        if(!scoreboard2.containsKey(player)) {

            Scoreboard board = null;

            board = Bukkit.getScoreboardManager().getNewScoreboard();



            Objective obj = board.getObjective("aaa");
            if (obj == null)
            {
                obj = board.registerNewObjective("aaa", "bbb");
                obj.setDisplayName("§e»  §3OITC  §e«");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            }
            Team map = board.registerNewTeam("map");

            map.addEntry("§8");
            obj.getScore("§6Map§8:").setScore(4);
            if(OITC.getInstance().isForceMap() || OITC.getInstance().getMapVoting().isOver()) {

                map.setPrefix("§b  " + OITC.getInstance().getMapName());
            }else {
                map.setPrefix("§b  Voting...");
            }
            obj.getScore("§8").setScore(3);
            obj.getScore("§c").setScore(2);
            Team countdown = board.registerNewTeam("countdown");
            countdown.addEntry("§l");
            Team countdown2 = board.registerNewTeam("countdown2");
            countdown2.addEntry("§d");
            if(OITC.getInstance().getLobbyCountdown().isStarted()) {
                countdown2.setPrefix("§eGame starts ");
                countdown2.setSuffix("§ein§8:");

                int totalSecs = OITC.getInstance().getLobbyCountdown().getTimer();
                int minutes =(totalSecs %3600)/60;
                int seconds = totalSecs %60;

                String timeString =String.format("%02d:%02d", minutes, seconds);
                countdown.setPrefix("§f" + timeString);
            }else{
                countdown2.setPrefix("§cWaiting for");
                countdown2.setSuffix("§c players....");
            }
            obj.getScore("§d").setScore(1);
            obj.getScore("§l").setScore(0);
            player.setScoreboard(board);
            scoreboard2.put(player, board);
            return;
        }


        Scoreboard board = scoreboard2.get(player);
        if(OITC.getInstance().getLobbyCountdown().isStarted()) {
            board.getTeam("countdown2").setPrefix("§eGame starts ");
            board.getTeam("countdown2").setSuffix("§ein§8:");

            int totalSecs = OITC.getInstance().getLobbyCountdown().getTimer();
            int minutes =(totalSecs %3600)/60;
            int seconds = totalSecs %60;

            String timeString =String.format("%02d:%02d", minutes, seconds);
            board.getTeam("countdown").setPrefix("§f" + timeString);
        }else{
            board.getTeam("countdown2").setPrefix("§cWaiting for");
            board.getTeam("countdown2").setSuffix("§cplayers....");
        }
        if(OITC.getInstance().isForceMap() || OITC.getInstance().getMapVoting().isOver()) {

            board.getTeam("map").setPrefix("§b  " + OITC.getInstance().getMapName());
        }else {
            board.getTeam("map").setPrefix("§b  Voting...");
        }
        scoreboard2.put(player, board);
    }
/*
    public void sendCustomScoreboard(Player player) {
        //Packets so you can use your own tab at the beginning
        CraftPlayer craftPlayer = (CraftPlayer) player;

        net.minecraft.server.v1_8_R3.Scoreboard scoreboard = new net.minecraft.server.v1_8_R3.Scoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.registerObjective("abc", IScoreboardCriteria.b);
        scoreboardObjective.setDisplayName("§e»  §3OITC  §e«");
        ScoreboardScore mapScore = null;
        ScoreboardScore mapScore2 = null;
        if(OITC.getInstance().isForceMap() || OITC.getInstance().getMapVoting().isOver()) {
            mapScore = new ScoreboardScore(scoreboard, scoreboardObjective, "§6Map§8:");


            mapScore2 = new ScoreboardScore(scoreboard, scoreboardObjective, "§b  " + OITC.getInstance().getMapName());
        }else {
            mapScore = new ScoreboardScore(scoreboard, scoreboardObjective, "§6Map§8:");


            mapScore2 = new ScoreboardScore(scoreboard, scoreboardObjective, "§b  Voting...");
        }
        mapScore2.setScore(3);
            mapScore.setScore(4);
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
*/
    private HashMap<Player, Scoreboard> scoreboard = new HashMap<>();
    public void updateScoreboard(Player player) {
        if(!scoreboard.containsKey(player)) {

            Scoreboard board = null;

            board = Bukkit.getScoreboardManager().getNewScoreboard();



            Objective obj = board.getObjective("aaa");
            if (obj == null)
            {
                obj = board.registerNewObjective("aaa", "bbb");
                obj.setDisplayName("§e»  §3OITC  §e«");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            }

            obj.getScore("§o").setScore(6);
            Team time2 = board.registerNewTeam("time2");
            time2.setPrefix("§cTime Left");
            time2.setSuffix("§8:");
            time2.addEntry("§8");
            obj.getScore("§8").setScore(7);
            Team time = board.registerNewTeam("time");
            int totalSecs = OITC.getInstance().getIngameCountdown().getTimer();
            if(totalSecs > 300) {
                totalSecs = 300;
            }
            int minutes =(totalSecs %3600)/60;
            int seconds = totalSecs %60;
            String timeString =String.format("%02d:%02d", minutes, seconds);
            time.setPrefix("§f"+timeString);
            time.addEntry("§6");
            obj.getScore("§6").setScore(6);
            obj.getScore("§eLeaders").setScore(5);
            Team leader1 = board.registerNewTeam("leader1");
            if(getStatsByTopPlayer(0).contains("-")) {
                String[] topPlayer = getStatsByTopPlayer(0).split("-");
                leader1.setPrefix(topPlayer[0] + "§7 - ");
                leader1.setSuffix(topPlayer[1]);
            }else{
                leader1.setPrefix(getStatsByTopPlayer(0));
            }
            leader1.addEntry("§f");
            obj.getScore("§f").setScore(4);
            Team leader2 = board.registerNewTeam("leader2");
            if(getStatsByTopPlayer(1).contains("-")) {
                String[] topPlayer = getStatsByTopPlayer(1).split("-");
                leader2.setPrefix(topPlayer[0] + "§7 - ");
                leader2.setSuffix(topPlayer[1]);
            }else{
                leader2.setPrefix(getStatsByTopPlayer(1));
            }
            leader2.addEntry("§b");
            obj.getScore("§b").setScore(3);
            Team leader3 = board.registerNewTeam("leader3");
            if(getStatsByTopPlayer(2).contains("-")) {
                String[] topPlayer = getStatsByTopPlayer(2).split("-");
                leader3.setPrefix(topPlayer[0] + "§7 - ");
                leader3.setSuffix(topPlayer[1]);
            }else{
                leader3.setPrefix(getStatsByTopPlayer(2));
            }
            leader3.addEntry("§c");
            obj.getScore("§c").setScore(2);
            Team leader4 = board.registerNewTeam("leader4");
            if(getStatsByTopPlayer(3).contains("-")) {
                String[] topPlayer = getStatsByTopPlayer(3).split("-");
                leader4.setPrefix(topPlayer[0] + "§7 - ");
                leader4.setSuffix(topPlayer[1]);
            }else{
                leader4.setPrefix(getStatsByTopPlayer(3));
            }
            leader4.addEntry("§e");
            obj.getScore("§e").setScore(1);
            Team leader5 = board.registerNewTeam("leader5");
            if(getStatsByTopPlayer(4).contains("-")) {
                String[] topPlayer = getStatsByTopPlayer(4).split("-");
                leader5.setPrefix(topPlayer[0] + "§7 - ");
                leader5.setSuffix(topPlayer[1]);
            }else{
                leader5.setPrefix(getStatsByTopPlayer(4));
            }
            leader5.addEntry("§l");
            obj.getScore("§l").setScore(0);
            player.setScoreboard(board);
            scoreboard.put(player, board);
            return;
        }


        Scoreboard board = scoreboard.get(player);
            int totalSecs = OITC.getInstance().getIngameCountdown().getTimer();
            if(totalSecs > 300) {
                totalSecs = 300;
            }
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            String timeString =String.format("%02d:%02d", minutes, seconds);
            board.getTeam("time").setPrefix("§f"+timeString);
            for(int i = 0; i <4; i++) {
                if(getStatsByTopPlayer(i).contains("-")) {
                    String[] topPlayer = getStatsByTopPlayer(i).split("-");
                    board.getTeam("leader"+(i+1)).setPrefix(topPlayer[0] + "§7 - ");
                    board.getTeam("leader"+(i+1)).setSuffix(topPlayer[1]);
                }else{
                    board.getTeam("leader"+(i+1)).setPrefix(getStatsByTopPlayer(i));
                }
            }
        scoreboard.put(player, board);
    }




    public List<Player> getTopPlayers() {
        List<Player> players = new ArrayList<>();
        Map<Player, Integer> sortedMap = sortByComparator(OITC.getInstance().getStatsOfTheGame().getKills(), false);
        sortedMap.keySet().forEach(player ->
                players.add(player));

           return players;
    }

    public String getStatsByTopPlayer(int i) {
        Map<Player, Integer> sortedMapDesc = sortByComparator(OITC.getInstance().getStatsOfTheGame().getKills(), false);
        List<Player> topPlayers = new ArrayList<>();
        sortedMapDesc.forEach((player, integer) ->
            topPlayers.add(player));
        if(topPlayers.size() <= i)
            return "§8No one";
        if(topPlayers == null || topPlayers.isEmpty()) {
            return "§8No one";
        }

        if(topPlayers.get(i) == null) {
            return "§8No one";
        }
        Player player = topPlayers.get(i);
        return "§f" + OITC.getInstance().getStatsOfTheGame().getKills(player) + "§7 - §1" + player.getName();
    }

    private static Map<Player, Integer> sortByComparator(Map<Player, Integer> map, final boolean order) {
        List<Map.Entry<Player, Integer>> list = new LinkedList<Map.Entry<Player, Integer>>(map.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Player, Integer>>() {
            public int compare(Map.Entry<Player, Integer> o1,
                               Map.Entry<Player, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Player, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Player, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public void onWin(boolean kills) {
        Bukkit.getScheduler().cancelAllTasks();
        if(kills) {
            Player player = OITC.getInstance().getGameUtils().getTopPlayers().get(0);
            Bukkit.getOnlinePlayers().forEach(all -> {
                OITC.getInstance().getTitleAPI().send(all, OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Win", false).replace("%PLAYER%", player.getName()), "",1,1,1);
                OITC.getInstance().getPlayerUtils().getPlayerManager(player.getUniqueId()).addValue("wins", 1);
                OITC.getInstance().getEndCountdown().runTaskTimer(OITC.getInstance(), 0,20);
            });
            return;
        }
        Player player = OITC.getInstance().getPlayerUtils().getPlayers().get(0);
        Bukkit.getOnlinePlayers().forEach(all -> {
            OITC.getInstance().getTitleAPI().send(all, OITC.getInstance().getMessageUtils().getMessageByConfig("Messages.Win", false).replace("%PLAYER%", player.getName()), "",1,1,1);
            OITC.getInstance().getPlayerUtils().getPlayerManager(player.getUniqueId()).addValue("wins", 1);
            OITC.getInstance().getEndCountdown().runTaskTimer(OITC.getInstance(), 0,20);
    });
    }


}
