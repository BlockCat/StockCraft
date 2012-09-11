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

import StockCraft.Permissions.PermissionCore;

import com.mysql.jdbc.Statement;

public class StockCraft extends JavaPlugin {
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	//public PermissionHandler PermissionsHandler = null;
	
	
	public static final Logger log = Logger.getLogger("Minecraft");
	public static Economy money = null;
	public PermissionCore pc;

	private BSQLinterface inter;

	public void onEnable() {

		new StockCraftProperties(this).load();
		
		pc = new PermissionCore(this);
		pc.load();
		
		setupEconomy();
		
		StockCraftDatabase SCD = new StockCraftDatabase(this);
		SCD.connecting();
		
		inter = StockCraftDatabase.inter;		
		Statement statement = inter.getStatement();

		if(statement != null)
		{
				if(!inter.tableExists("stocks")){
					inter.addTable("stocks", "name:char(50)" , "stockname:char(50)" , "sumpaid:float" , "amount:int");
					
				}
				if(!inter.tableExists("stockstats")){
					inter.addTable("stockstats", "name:char(50)","profit:float");
				}
				if(!inter.tableExists("idtable")){
					inter.addTable("idtable", "longid:char(50)","shortid:char(50)");
				}
		}


		// Register our events

		this.getServer().getPluginManager().registerEvents(new StockCraftPlayerListener(this), this);

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		PluginDescriptionFile pdfFile = this.getDescription();
		StockCraftPermissions.initialize(getServer(), pc);
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
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

	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			money = economyProvider.getProvider();
		}
		return (money != null);
	}
}
