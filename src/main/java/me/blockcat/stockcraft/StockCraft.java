/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package me.blockcat.stockcraft;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import me.blockcat.stockcraft.connectUtils.ConnectionFactory;
import me.blockcat.stockcraft.connectUtils.DatabaseHandler;
import me.blockcat.stockcraft.connectUtils.ObjectPool;
import me.blockcat.stockcraft.connectUtils.Pool;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.Connection;

public class StockCraft extends JavaPlugin {
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	//public PermissionHandler PermissionsHandler = null;

	public static Permission permissions = null;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static Economy money = null;
	public static Pool<Connection> connectionPool = null;;
	public static ExecutorService threadPool = null;

	public void onEnable() {
		
		threadPool = Executors.newCachedThreadPool();

		new Config(this).load();
		setupEconomy();
		setupPermissions();
		StockCraftDatabase SCD = new StockCraftDatabase(this);

		connectionPool = new ObjectPool<Connection>();
		connectionPool.setFactory(new ConnectionFactory());

		DatabaseHandler.checkTables();

		// Register our events

		this.getServer().getPluginManager().registerEvents(new StockCraftPlayerListener(this), this);

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		PluginDescriptionFile pdfFile = this.getDescription();
		StockCraftPermissions.initialize(getServer());
		System.out.println("[StockCraft] Version: " + pdfFile.getVersion() + " is enabled!" );
	}

	public void onDisable() {
		threadPool.shutdownNow();
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
	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permissions = permissionProvider.getProvider();
		}
		return (permissions != null);
	}
}
