package StockCraft.Permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionCore {

	private JavaPlugin plugin;
	private String[] PSystems = {"PermissionsEx","bPermissions","GroupManager"};
	private List<String> PermissionSystems = Arrays.asList(PSystems);
	private List<PermissionHandler> active = new ArrayList<PermissionHandler>();

	private Class<? extends PermissionHandler>[] handlers = new Class[] {PermissionsExHandler.class, BpermissionsHandler.class}; 

	public PermissionCore (JavaPlugin plugin) {
		this.plugin = plugin;
		load();
	}	

	public void load() {
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

		for (Plugin plugin : plugins) {
			String name = plugin.getName();
			if (PermissionSystems.contains(name)) {
				System.out.println(name + "is there!?!?!?!?!?!?!?!?!?!??!?!?!?!?!?!?!?");
				int i = PermissionSystems.indexOf(name);
				System.out.println(handlers[i].getName());
				try {
				handlers[i].newInstance().setEnabled(true);
				
				active.add(handlers[i].newInstance());
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
