package me.devcode.oitc.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import lombok.SneakyThrows;
import me.devcode.oitc.OITC;


public class MySQLMethods {

	@SneakyThrows
public void createPlayer(String uuid) {
		
		if(!OITC.getInstance().getStats().getBooleanMethod("oitc", "uuid", uuid)) {

		PreparedStatement createPlayer = OITC.getInstance().getMysql().prepare("INSERT INTO oitc(uuid, kills, deaths, wins, games, points) VALUES (?, ?, ?, ?, ?, ?);");
		try {
			createPlayer.setString(1, uuid);
			createPlayer.setInt(2, 0);
			createPlayer.setInt(3, 0);
			createPlayer.setInt(4, 0);
			createPlayer.setInt(5, 0);
			createPlayer.setInt(6, 100);
			createPlayer.executeUpdate();
			createPlayer.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
	}	
	
public int getRank(String table, String uuid) {
	int count = 0;
	if(OITC.getInstance().getStats().getBooleanMethod(table, "uuid", uuid)) {
		ResultSet rs = null;
		try{
			
			PreparedStatement statement = OITC.getInstance().getMysql().prepare("SELECT * FROM " + table + " ORDER BY wins DESC");
			rs = statement.executeQuery();

			while(rs.next()) {
				
				
				count++;
				String namedUUID = rs.getString("uuid");
				UUID uuid1 = UUID.fromString(namedUUID);
				
				if(uuid1.toString().equals(uuid)) {
					statement.close();
					return count;
				}
			}
		}catch(SQLException e) {
			
		}
		
	}
	return count;
}

    public Object getValue(String uuid, String key) {
        return OITC.getInstance().getStats().getIntMethod("oitc", "uuid",uuid,key);
    }


		
	public void setAllMethod(String table, String from, String uuid,Object kills, Object deaths, Object wins, Object games, Object coins) {
		PreparedStatement statement = OITC.getInstance().getMysql().prepare(
				"UPDATE " + table + " SET kills = ?, deaths = ?, wins = ?, games = ?, points = ? WHERE " + from + "= ?;");
		try {
			statement.setInt(1, (int) kills);
		
		statement.setInt(2, (int)deaths);
		statement.setInt(3, (int)wins);
		statement.setInt(4, (int)games);
			statement.setInt(5,(int) coins);
		statement.setString(6, uuid);
		statement.executeUpdate();
		statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
