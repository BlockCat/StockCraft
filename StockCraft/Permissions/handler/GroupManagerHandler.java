package StockCraft.Permissions.handler;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import StockCraft.Permissions.PermissionHandler;

public class GroupManagerHandler extends PermissionHandler {

	private boolean enabled;
	private GroupManager groupManager;

	@Override
	public void addGroup(Player player, String s) {
		
	}

	@Override
	public boolean hasPermissions(Player player, String node) {
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player);
		if (handler == null)
		{
			return false;
		}
		return handler.has(player, node);
	}

	@Override
	public String getName() {
		return "GroupManager";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		if (enabled) {
			final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
			final Plugin GMplugin = pluginManager.getPlugin("GroupManager");
	 
			if (GMplugin != null && GMplugin.isEnabled())
			{
				groupManager = (GroupManager)GMplugin;
	 
			}
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
