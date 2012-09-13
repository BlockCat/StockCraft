package StockCraft.Permissions.handler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import StockCraft.Permissions.PermissionHandler;
import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

public class BpermissionsHandler extends PermissionHandler {

	private boolean enabled = false;
	
	@Override
	public void addGroup(Player player, String s) {
		ApiLayer.addGroup(player.getWorld().getName(), CalculableType.USER, player.getName(), s);
	}

	@Override
	public boolean hasPermissions(Player player, String node) {
		return ApiLayer.hasPermission(player.getName(), CalculableType.USER, player.getName(), node);
	}

	@Override
	public void setEnabled(boolean b) {
		
		enabled = b;
	}

	@Override
	public boolean isEnabled() {
		System.out.println(ChatColor.WHITE + "[StockCraft] bPermsissions found.");
		return enabled;
	}

	@Override
	public String getName() {
		return "bPermissions";
	}

}
