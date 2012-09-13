package StockCraft.Permissions.handler;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import StockCraft.Permissions.PermissionHandler;

public class PermissionsExHandler extends PermissionHandler{
	
	boolean enabled = false;

	@Override
	public void addGroup(Player player, String s) {
		PermissionUser user = PermissionsEx.getUser(player);
		user.addGroup(s);		
	}

	@Override
	public boolean hasPermissions(Player player, String node) {
		PermissionUser user = PermissionsEx.getUser(player);
		return user.has(node);
	}

	@Override
	public boolean isEnabled() {		
		return enabled;
	}

	@Override
	public void setEnabled(boolean b) {
		System.out.println("[StockCraft] PermissionsEx found.");
		enabled = b;		
	}

	@Override
	public String getName() {
		return "PermissionsEx";
	}

}
