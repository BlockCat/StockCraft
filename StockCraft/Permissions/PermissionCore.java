package StockCraft.Permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import StockCraft.Permissions.handler.BpermissionsHandler;
import StockCraft.Permissions.handler.PermissionsExHandler;

public class PermissionCore {

	private JavaPlugin plugin;
	private String[] PSystems = {"PermissionsEx","bPermissions","GroupManager"};
	private List<String> PermissionSystems = Arrays.asList(PSystems);
	private List<PermissionHandler> active = new ArrayList<PermissionHandler>();
	private List<String> activeNames = new ArrayList<String>();

	private Class<? extends PermissionHandler>[] handlers = new Class[] {PermissionsExHandler.class, BpermissionsHandler.class}; 

	public PermissionCore (JavaPlugin plugin) {
		this.plugin = plugin;
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
					if (!activeNames.contains(handlers[i].newInstance())) {
						handlers[i].newInstance().setEnabled(true);
						active.add(handlers[i].newInstance());
						activeNames.add(handlers[i].getName());
						return;
					}

				} catch (Exception e) {
					continue;
				}
				return;
			}
		}
	}

	public PermissionHandler getHandler() {
		PermissionHandler handler = null;
		for (PermissionHandler ph : active) {
			handler = ph;
		}
		return handler;
	}

}
