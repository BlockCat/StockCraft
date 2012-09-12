package StockCraft.Economy.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import StockCraft.Economy.EconomyHandler;
import ca.agnate.EconXP.EconXP;

public class EconXPHandler extends EconomyHandler{

	private boolean enabled;
	private EconXP econXP;

	@Override
	public boolean hasMoney(Player player, double money) {
		return econXP.hasExp(player, (int) money);
	}

	@Override
	public void subtract(Player player, double money) {
		econXP.removeExp(player, (int) money);
	}

	@Override
	public void add(Player player, double money) {
		econXP.addExp(player, (int) money);
	}

	@Override
	public double getBalance(Player player) {
		return econXP.getExp(player);
	}

	@Override
	public String getName() {
		return "EconXP";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		
		if(enabled) {
			econXP = (EconXP) Bukkit.getServer().getPluginManager().getPlugin("EconXP");
		}
		
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
