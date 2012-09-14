package StockCraft.Permissions.handler;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Permissions.PermissionHandler;

public class GroupManagerHandler extends PermissionHandler {

	private boolean enabled;
	private GroupManager groupManager;

	@Override
	public void addGroup(Player player, String s) {

	}

	@Override
	public boolean hasPermissions(Player player, String node) {
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(player.getName());
		
		if (handler == null) {
			return false;
		}
		return handler.permission(player.getName(), node);
	}

	@Override
	public String getName() {
		return "GroupManager";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		if (enabled) {
			if (groupManager == null) {
				Plugin perms = Bukkit.getServer().getPluginManager().getPlugin("GroupManager");

				if (perms != null) {
					if (perms.isEnabled()) {
						groupManager = (GroupManager) perms;
						System.out.println("[StockCraft] found and enabled: " + getName());                        
					}
				}
			}
		}
	}


	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
