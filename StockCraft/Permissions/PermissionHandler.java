package StockCraft.Permissions;

import org.bukkit.entity.Player;

public abstract class PermissionHandler {

	public abstract void addGroup(Player player, String s);
	public abstract boolean hasPermissions(Player player, String node);
	public abstract String getName();
	public abstract void setEnabled(boolean b);
	public abstract boolean isEnabled();
	
}
