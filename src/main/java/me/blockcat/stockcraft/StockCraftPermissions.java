/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package me.blockcat.stockcraft;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class StockCraftPermissions {
	public static boolean permissionsEnabled = false;
	private static volatile StockCraftPermissions instance;

	public static void initialize(Server server) {
		Plugin test = server.getPluginManager().getPlugin(StockCraft.permissions.getName());
		if (test != null) {
			Logger log = Logger.getLogger("Minecraft");
			permissionsEnabled = true;
			log.log(Level.INFO, "[StockCraft] Permissions enabled.");
		} else {
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.SEVERE, "[StockCraft] Permissions isn't loaded, there are no restrictions.");
		}
	}
	private boolean permission(Player player, String string) {
		if (player.isOp()) return true;
		if (permissionsEnabled) {
			return StockCraft.permissions.playerHas(player, string);
		} else {
			return false;
		}
	}
	public static StockCraftPermissions getInstance() {
		if (instance == null) {
			instance = new StockCraftPermissions();
		}
		return instance;
	}

	public boolean addid(Player player) {
		return permission(player, "stockcraft.addid");
	}
	public boolean removeid(Player player) {
		return permission(player, "stockcraft.removeid");
	}

	public boolean course(Player player) {
		return permission(player, "stockcraft.commands.course") || permission(player, "stockcraft.commands.*") || permission(player, "stockcraft.*");
	}
	public boolean stockbuy(Player player) {
		return permission(player, "stockcraft.commands.stockbuy") || permission(player, "stockcraft.commands.*") || permission(player, "stockcraft.*");
	}
	public boolean stocksell(Player player) {
		return permission(player, "stockcraft.commands.stocksell") || permission(player, "stockcraft.commands.*") || permission(player, "stockcraft.*");
	}
	public boolean stocktop(Player player) {
		return permission(player, "stockcraft.commands.stocktop") || permission(player, "stockcraft.commands.*") || permission(player, "stockcraft.*");
	}
	public boolean stocks(Player player) {
		return permission(player, "stockcraft.commands.stocks") || permission(player, "stockcraft.commands.*") || permission(player, "stockcraft.*");
	}
	public boolean ids(Player player) {
		return permission(player, "stockcraft.commands.ids") || permission(player, "stockcraft.commands.*") || permission(player, "stockcraft.*");
	}
	public boolean stockhelp(Player player) {
		return permission(player, "stockcraft.commands.stockhelp") || permission(player, "stockcraft.commands.*") || permission(player, "stockcraft.*");
	}


}