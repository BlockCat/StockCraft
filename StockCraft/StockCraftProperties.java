/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package StockCraft;

import org.bukkit.configuration.file.FileConfiguration;


public class StockCraftProperties{	

	public static Boolean perm;
	public static Boolean shorten;
	public static Boolean iconomy5;
	public static Double fee;
	public static Double minimumfee;

	private StockCraft plugin;

	StockCraftProperties(StockCraft instance){
		plugin = instance;		
	}

	public void load(){		
		final FileConfiguration properties = plugin.getConfig();

		properties.options().header("StockCraft");

		properties.addDefault("detailed permissions",false);
		properties.addDefault("shorten",false);
		properties.addDefault("Vault",true);
		properties.addDefault("fee",0);
		properties.addDefault("minimumfee", 0);

		perm = properties.getBoolean("detailedpermissions",false);
		shorten = properties.getBoolean("shorten",false);
		iconomy5 = properties.getBoolean("Vault",true);
		fee = properties.getDouble("fee",0);
		minimumfee = properties.getDouble("minimumfee", 0);

		properties.options().copyDefaults(true);
		plugin.saveConfig();
	}	






}