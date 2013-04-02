/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package me.blockcat.stockcraft;

import org.bukkit.configuration.file.FileConfiguration;


public class Config{	

	public static Boolean perm;
	public static Boolean shorten;
	public static Boolean iconomy5;
	public static Double fee;
	public static Double minimumfee;
	public static String url;
	public static String username;
	public static String pass;

	private StockCraft plugin;

	Config(StockCraft instance){
		plugin = instance;		
	}

	public void load(){		
		final FileConfiguration config = plugin.getConfig();

		config.options().header("StockCraft");

		config.addDefault("shorten",false);
		config.addDefault("Vault",true);
		config.addDefault("fee",0);
		config.addDefault("minimumfee", 0);
		config.addDefault("database.host", "localhost");
		config.addDefault("database.port", "3306");
		config.addDefault("database.database", "stockcraft");
		config.addDefault("database.username", "root");
		config.addDefault("database.password", "");

		shorten = config.getBoolean("shorten",false);
		iconomy5 = config.getBoolean("Vault",true);
		fee = config.getDouble("fee",0);
		minimumfee = config.getDouble("minimumfee", 0);
		
		username = config.getString("database.username", "root");
		pass = config.getString("database.password", "");
		
		//URL start
		
		String host = config.getString("database.host", "localhost");
		String port = config.getString("database.port", "3306");
		String database = config.getString("database.database", "stockcraft");
		url = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
		
		//URL end

		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public static String getURL() {
		return url;
	}
	
	public static String getUsername() {
		return username;
	}
	
	public static String getPassword() {
		return pass;
	}






}