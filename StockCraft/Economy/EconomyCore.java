package StockCraft.Economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import StockCraft.Economy.handler.CraftConomyHandler;
import StockCraft.Economy.handler.EconXPHandler;
import StockCraft.Economy.handler.iConomyHandler;

public class EconomyCore {

	private JavaPlugin plugin;
	private String[] PSystems = {"iConomy","Craftconomy3", "EconXP"};
	private List<String> PermissionSystems = Arrays.asList(PSystems);
	private List<EconomyHandler> active = new ArrayList<EconomyHandler>();
	private List<String> activeNames = new ArrayList<String>();

	private Class<? extends EconomyHandler>[] handlers = new Class[] {iConomyHandler.class, CraftConomyHandler.class, EconXPHandler.class}; 

	public EconomyCore (JavaPlugin plugin) {
		this.plugin = plugin;
		//load();
		plugin.getServer().getPluginManager().registerEvents(new Listener() {

			@EventHandler
			public void onPluginEnable(PluginEnableEvent event) {
				load();
			}

		}, plugin);
	}	

	public void load() {
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

		for (Plugin plugin : plugins) {
			String name = plugin.getName();
			if (PermissionSystems.contains(name)) {
				int i = PermissionSystems.indexOf(name);
				//System.out.println(handlers[i].getName());
				try {
					if (!activeNames.contains(handlers[i].getName())) {
						System.out.println("[StockCraft] Economy plugin found!");
						
						handlers[i].newInstance().setEnabled(true);
						active.add(handlers[i].newInstance());
						
						activeNames.add(handlers[i].getName());
						return;
					}
				} catch (Exception e) {
					System.out.println("[StockCraft] Economy failed.");
					continue;
				}
				return;
			}
		}
	}

	public EconomyHandler getHandler() {
		EconomyHandler handler = null;
		for (EconomyHandler ph : active) {
			handler = ph;
		}
		return handler;
	}

}
