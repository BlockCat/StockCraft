package me.blockcat.stockcraft.connectUtils;

import java.sql.DriverManager;

import me.blockcat.stockcraft.Config;

import com.mysql.jdbc.Connection;

public class ConnectionFactory implements PoolObjectFactory<Connection>{

	public Connection newObject() {
		Connection conn = null;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = (Connection) DriverManager.getConnection(Config.getURL(), Config.getUsername(), Config.getPassword());
			return conn;
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("[StockCraft] Couldn't get connection, wrong login?");
			return null;
		}
	}

}
