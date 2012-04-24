/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package StockCraft;

import java.util.HashMap;
import java.util.logging.Logger;

import me.BlockCat.bukkitSQL.BSQLinterface;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.mysql.jdbc.Statement;

public class StockCraft extends JavaPlugin {
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	public static Permission permission;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static Economy money = null;

	private BSQLinterface inter;

	public void onEnable() {

		new StockCraftProperties(this).load();

		setupEconomy();
		StockCraftDatabase SCD = new StockCraftDatabase(this);
		SCD.connecting();

		inter = StockCraftDatabase.inter;		
		Statement statement = inter.getStatement();

		if(statement != null)
		{
			if(inter.tableExists("stocks")){
				inter.addTable("stocks", "name:char(50)" , "stockname:char(50)" , "sumpaid:float" , "amount:int");
			}
			if(inter.tableExists("stockstats")){
				inter.addTable("stockstats", "name:char(50)","profit:float");
			}
			if(inter.tableExists("idtable")){
				inter.addTable("idtable", "longid:char(50)","shortid:char(50)");
			}
		}

		// Register our events

		this.getServer().getPluginManager().registerEvents(new StockCraftPlayerListener(this), this);

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}

	public void onDisable() {
		// TODO: Place any custom disable code here

		// NOTE: All registered events are automatically unregistered when a plugin is disabled

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		//System.out.println("Goodbye world!");
	}
	public boolean isDebugging(final Player player) {
		if (debugees.containsKey(player)) {
			return debugees.get(player);
		} else {
			return false;
		}
	}

	public void setDebugging(final Player player, final boolean value) {
		debugees.put(player, value);
	}
	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			money = economyProvider.getProvider();
		}
		return (money != null);
	}
}
