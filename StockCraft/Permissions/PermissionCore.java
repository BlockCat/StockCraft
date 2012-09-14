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
import StockCraft.Permissions.handler.GroupManagerHandler;
import StockCraft.Permissions.handler.PermissionsExHandler;

public class PermissionCore {

	private String[] PSystems = {"PermissionsEx","bPermissions","GroupManager"};
	private List<String> PermissionSystems = Arrays.asList(PSystems);
	private List<PermissionHandler> active = new ArrayList<PermissionHandler>();
	private List<String> activeNames = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	private Class<? extends PermissionHandler>[] handlers = new Class[] {PermissionsExHandler.class, BpermissionsHandler.class, GroupManagerHandler.class}; 

	public PermissionCore (JavaPlugin plugin) {
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
				try {
					if (!activeNames.contains(handlers[i].newInstance())) {
						PermissionHandler h1 = handlers[i].newInstance();
						h1.setEnabled(true);
						active.add(h1);
						activeNames.add(h1.getName());
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
